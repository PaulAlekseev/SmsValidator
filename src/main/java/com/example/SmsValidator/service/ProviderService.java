package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.provider.*;
import com.example.SmsValidator.dto.ModemDto;
import com.example.SmsValidator.dto.TaskDto;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.entity.TaskEntity;
import com.example.SmsValidator.exception.customexceptions.provider.ProviderSessionBusyException;
import com.example.SmsValidator.exception.customexceptions.provider.ProviderSessionNotFoundException;
import com.example.SmsValidator.exception.customexceptions.socket.ModemProviderSessionDoesNotExistException;
import com.example.SmsValidator.repository.ModemProviderSessionEntityRepository;
import com.example.SmsValidator.repository.TaskEntityRepository;
import com.example.SmsValidator.repository.UsedServiceTypeEntityRepository;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.ModemSocketHandler;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.container.ProviderReadyOutContainer;
import com.example.SmsValidator.socket.container.ProviderStopOutContainer;
import com.example.SmsValidator.specification.ModemSpecification;
import com.example.SmsValidator.specification.TaskSpecification;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final Gson gson = new Gson();

    private final ModemService modemService;
    private final ModemProviderSessionEntityRepository modemProviderSessionEntityRepository;
    private final ModemSocketHandler modemSocketHandler;
    private final UsedServiceTypeEntityRepository usedServiceTypeEntityRepository;
    private final TaskEntityRepository taskEntityRepository;

    public ProviderTasksFromProviderIdResponse getTasksFromModemId(Long modemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Specification<TaskEntity> specification = TaskSpecification.hasUser_Email(authentication.getName())
                .and(TaskSpecification.hasModemEntity_Id(modemId));

        ModelMapper modelMapper = new ModelMapper();
        List<TaskDto> tasks = taskEntityRepository.findAll(specification)
                .stream()
                .map((entity) -> modelMapper.map(entity, TaskDto.class))
                .toList();

        return ProviderTasksFromProviderIdResponse.builder()
                .tasks(tasks)
                .build();
    }

    public int setModemProviderBusy(ModemProviderSessionEntity modemProviderSession) {
        return modemProviderSessionEntityRepository.
                updateBusyBySocketIdAndActiveTrue(true, modemProviderSession.getSocketId());
    }

    public ProviderActivateBaseResponse activate()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(false);
        WebSocketSession session = getSocketSessionBySocketId(modemProviderSession.getSocketId());
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.START_PROVIDER, gson.toJson(new ProviderReadyOutContainer())));
        } catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    public ProviderActivateBaseResponse stop()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(true);
        WebSocketSession session = getSocketSessionBySocketId(modemProviderSession.getSocketId());
        disconnectNotBusyModems(modemProviderSession);
        setProviderBusy(modemProviderSession);
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.STOP_PROVIDER, gson.toJson(new ProviderStopOutContainer())));
        } catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    public WebSocketSession getSocketSessionBySocketId(String socketId) throws ModemProviderSessionDoesNotExistException {
        WebSocketSession socketSession = modemSocketHandler.getSessions().get(socketId);
        if (socketSession == null) throw new ModemProviderSessionDoesNotExistException("Modem provider is not active");
        return socketSession;
    }

    public int setProviderBusy(ModemProviderSessionEntity modemProviderSession) {
        return modemProviderSessionEntityRepository.
                updateBusyBySocketIdAndActiveTrue(true, modemProviderSession.getSocketId());
    }

    public int disconnectNotBusyModems(ModemProviderSessionEntity providerSession) {
        return modemService.disconnectNotBusyModems(providerSession);
    }

    public ProviderActivateBaseResponse saveModems()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(false);
        WebSocketSession session = getSocketSessionBySocketId(modemProviderSession.getSocketId());
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.START_SAVE_MODEMS, gson.toJson(new ProviderReadyOutContainer())));
            modemService.disconnectModemsOnSave(modemProviderSession);
        } catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    private ModemProviderSessionEntity getProviderSessionFromContext()
            throws ProviderSessionNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return modemProviderSessionEntityRepository
                .findByUser_UsernameAndActiveTrue(authentication.getName())
                .orElseThrow(() -> new ProviderSessionNotFoundException("Provider session not found"));
    }

    private ModemProviderSessionEntity getProviderSessionFromContext(Boolean requiredBusy)
            throws ProviderSessionNotFoundException, ProviderSessionBusyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModemProviderSessionEntity sessionEntity = modemProviderSessionEntityRepository
                .findByUser_EmailAndActiveTrue(authentication.getName())
                .orElseThrow(() -> new ProviderSessionNotFoundException("Provider session not found"));
        if (sessionEntity.getBusy() == requiredBusy) {
            String busyAddition = "";
            if (!requiredBusy) busyAddition = "not ";
            throw new ProviderSessionBusyException("Provider is " + busyAddition + "busy");
        }
        return sessionEntity;
    }

    public ProviderModemsSuccessResponse getProvidersWorkingModems() {
        ModelMapper mapper = new ModelMapper();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Specification<ModemEntity> modemSpecification = ModemSpecification.hasModemProviderSession_User_Email(email)
                .and(ModemSpecification.withTasks())
                .and(ModemSpecification.hasModemProviderSessionEntity_Active(true))
                .and(ModemSpecification.hasModemProviderSessionEntity_Busy(false));
        List<ModemDto> data = modemService
                .getModemsWithSpecification(modemSpecification)
                .stream().map(entity -> mapper.map(entity, ModemDto.class))
                .toList();
        ProviderModemsSuccessResponse response = ProviderModemsSuccessResponse.builder()
                .data(data)
                .build();
        return response;
    }

    public ProviderModemsSuccessResponse getProvidersModemsOnRevenue(int revenue) {
        ModelMapper modelMapper = new ModelMapper();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Specification<ModemEntity> modemSpecification = ModemSpecification
                .hasModemProviderSession_User_Email(email)
                .and(ModemSpecification.hasRevenueMoreThan(revenue));
        List<ModemDto> data = modemService
                .getModemsWithSpecification(modemSpecification)
                .stream().map(entity -> modelMapper.map(entity, ModemDto.class))
                .toList();
        return ProviderModemsSuccessResponse.builder()
                .data(data)
                .build();
    }

    public ProviderModemsSuccessResponse disconnectModemsByCriteria(ProviderModemDisconnectCriteriaRequest request) throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException, IOException {
        ModemProviderSessionEntity providerSession = getProviderSessionFromContext(true);

        Specification<ModemEntity> modemSpecification = ModemSpecification
                .hasBusy(false)
                .and(ModemSpecification.hasModemProviderSessionId(providerSession.getId()));
        if (request.isByService())
            modemSpecification.and(modemService.formServiceAbbreviationSpecification(request.getServices()));
        if (request.isByRevenue()) modemSpecification.and(ModemSpecification.hasRevenueMoreThan(request.getRevenue()));

        ModelMapper modelMapper = new ModelMapper();
        List<ModemDto> modems = modemService
                .getModemsWithSpecification(modemSpecification)
                .stream()
                .map((entity) -> modelMapper.map(entity, ModemDto.class))
                .toList();

        WebSocketSession socketSession = getSocketSessionBySocketId(providerSession.getSocketId());

        socketSession.sendMessage(MessageFormer.formMessage(
                OutCommands.DISCONNECT_MODEMS,
                gson.toJson(modems)
        ));
        return ProviderModemsSuccessResponse.builder()
                .data(modems)
                .build();
    }

    public ProviderModemsSuccessResponse getModemsByCriteria(int revenue, String services) throws ProviderSessionNotFoundException, ProviderSessionBusyException {
        ModemProviderSessionEntity providerSession = getProviderSessionFromContext(true);
        System.out.println(revenue);
        System.out.println(services);

        Specification<ModemEntity> modemSpecification = ModemSpecification
                .hasBusy(false)
                .and(ModemSpecification.hasModemProviderSessionId(providerSession.getId()))
                .and((modemService.formServiceAbbreviationSpecification(services))
                .or(ModemSpecification.hasRevenueMoreThan(revenue)));

        ModelMapper modelMapper = new ModelMapper();
        List<ModemDto> modems = modemService
                .getModemsWithSpecification(modemSpecification)
                .stream()
                .map((entity) -> modelMapper.map(entity, ModemDto.class))
                .toList();
        return ProviderModemsSuccessResponse.builder()
                .data(modems)
                .build();
    }

    public ProviderModemsSuccessResponse disconnectModems(ProviderModemDisconnectRequest request) throws ProviderSessionNotFoundException, ProviderSessionBusyException, ModemProviderSessionDoesNotExistException, IOException {
        ModemProviderSessionEntity providerSession = getProviderSessionFromContext(true);
        List<String> imsis = request.getModem()
                .stream()
                .map(ModemEntity::getIMSI)
                .toList();


        ModelMapper modelMapper = new ModelMapper();
        Specification<ModemEntity> specification = ModemSpecification.hasBusy(false)
                .and(ModemSpecification.hasImsiIn(imsis))
                .and(ModemSpecification.hasModemProviderSessionId(providerSession.getId()));
        List<ModemDto> modems = modemService.getModemsWithSpecification(specification)
                .stream()
                .map((entity) -> modelMapper.map(entity, ModemDto.class))
                .toList();

        WebSocketSession socketSession = getSocketSessionBySocketId(providerSession.getSocketId());

        socketSession.sendMessage(MessageFormer.formMessage(
                OutCommands.DISCONNECT_MODEMS,
                gson.toJson(modems)
        ));
        return ProviderModemsSuccessResponse.builder()
                .data(modems)
                .build();
    }
}
