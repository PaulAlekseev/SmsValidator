package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.provider.*;
import com.example.SmsValidator.dto.ModemDto;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.exception.customexceptions.provider.ProviderSessionBusyException;
import com.example.SmsValidator.exception.customexceptions.provider.ProviderSessionNotFoundException;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.ModemProviderSessionEntityRepository;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.ModemSocketHandler;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.container.ProviderReadyOutContainer;
import com.example.SmsValidator.socket.container.ProviderStopOutContainer;
import com.example.SmsValidator.specification.ModemSpecification;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final Gson gson = new Gson();

    private final ModemSocketHandler modemSocketHandler;
    private final SocketService socketService;
    private final ModemEntityRepository modemEntityRepository;
    private final ModemProviderSessionEntityRepository modemProviderSessionEntityRepository;
    private final ModemService modemService;

    public ProviderActivateBaseResponse activate()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(true);
        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.START_PROVIDER, gson.toJson(new ProviderReadyOutContainer())));
        } catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    public ProviderActivateBaseResponse stop()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(true);
        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
        socketService.disconnectNotBusyModems(modemProviderSession);
        socketService.setModemProviderBusy(modemProviderSession);
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.STOP_PROVIDER, gson.toJson(new ProviderStopOutContainer())));
        } catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    public ProviderActivateBaseResponse saveModems()
            throws ProviderSessionNotFoundException, ProviderSessionBusyException {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(false);
        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.START_SAVE_MODEMS, gson.toJson(new ProviderReadyOutContainer())));
            modemEntityRepository.
                    updateModemProviderSessionEntityByModemProviderSessionEntityAndBusyFalse(null, modemProviderSession);
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
                .findByUser_UsernameAndActiveTrue(authentication.getName())
                .orElseThrow(() -> new ProviderSessionNotFoundException("Provider session not found"));
        if (sessionEntity.getBusy() != requiredBusy) {
            String busyAddition = "";
            if (requiredBusy) busyAddition = "not ";
            throw new ProviderSessionBusyException("Provider is " + busyAddition + "busy");
        }
        return sessionEntity;
    }

    public ProviderModemsSuccessResponse getProvidersWorkingModems() {
        ModelMapper mapper = new ModelMapper();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Specification<ModemEntity> modemSpecification = ModemSpecification.hasModemProviderSession_User_Email(email)
                .and(ModemSpecification.withTasks());
        List<ModemDto> data = modemEntityRepository
                .findAll(modemSpecification)
                .stream().map(entity -> mapper.map(entity, ModemDto.class))
                .toList();
        return ProviderModemsSuccessResponse.builder()
                .data(data)
                .build();
    }

    public ProviderModemsSuccessResponse getProvidersModemsOnRevenue(int revenue) {
        ModelMapper modelMapper = new ModelMapper();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Specification<ModemEntity> modemSpecification = ModemSpecification
                .hasModemProviderSession_User_Email(email)
                .and(ModemSpecification.hasRevenueMoreThan(revenue));
        List<ModemDto> data = modemEntityRepository
                .findAll(modemSpecification)
                .stream().map(entity -> modelMapper.map(entity, ModemDto.class))
                .toList();
        return ProviderModemsSuccessResponse.builder()
                .data(data)
                .build();
    }

//    public ProviderModemsSuccessResponse disconnectModemsByCriteria(ProviderModemDisconnectCriteriaRequest request ) {
//
//    }

//    public void thing(ModemEntity modemEntity)
//            throws ProviderSessionNotFoundException, CouldNotFindSuchModemException {
//        ModemProviderSessionEntity modemProviderSession = modemProviderSessionEntityRepository.find
//        if (socketService.disconnectModemFromProvider() < 1) {
//            throw new CouldNotFindSuchModemException("Could not find such modem");
//        }
//        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
//
//    }
}
