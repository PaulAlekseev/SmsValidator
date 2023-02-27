package com.example.SmsValidator.service;

import com.example.SmsValidator.auth.JwtTokenProvider;
import com.example.SmsValidator.entity.*;
import com.example.SmsValidator.exception.customexceptions.provider.CouldNotFindSuchModemException;
import com.example.SmsValidator.exception.customexceptions.socket.ModemProviderSessionAlreadyActiveException;
import com.example.SmsValidator.exception.customexceptions.socket.ModemProviderSessionDoesNotExistException;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.repository.*;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.ModemSocketHandler;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.container.CheckProviderOutContainer;
import com.example.SmsValidator.socket.container.ModemCheckOutContainer;
import com.example.SmsValidator.socket.container.UpdateModemOnPortContainer;
import com.example.SmsValidator.specification.TaskSpecification;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final ModemProviderSessionEntityRepository modemProviderSessionRepo;
    private final ModemEntityRepository modemEntityRepository;
    private final TaskEntityRepository taskEntityRepository;
    private final TaskService taskService;
    private final MessageEntityRepository messageEntityRepository;
    private final ServiceTypeEntityRepository serviceTypeEntityRepository;
    private final UsedServiceTypeEntityRepository usedServiceTypeEntityRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    public TaskEntity getTaskById(Long id) {
        return taskEntityRepository.findById(id).get();
    }

    @Deprecated
    public void updateUsedServiceOnSuccess(TaskEntity task) {
        UsedServiceTypeEntity usedServiceType = usedServiceTypeEntityRepository
                .findFirstByModemEntity_IdAndServiceType_Id(task.getModemEntity().getId(), task.getServiceTypeEntity().getId());
        if (usedServiceType == null) {
            return;
        } else {
            int plusTimes = task.isSuccess() ? 1 : 0;
            usedServiceType.setTimesUsed(usedServiceType.getTimesUsed() + plusTimes);
            if (plusTimes > 0) {
                usedServiceType.setLastTimeUsed(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        }
        usedServiceTypeEntityRepository.save(usedServiceType);
        task.setUsedServiceTypeEntity(usedServiceType);
        taskEntityRepository.save(task);
    }

    public void updateModemOnSuccess(TaskEntity task) {
        ModemEntity modem = task.getModemEntity();
        modem.addService(task.getServiceName());
        modemEntityRepository.save(modem);
    }

    public int setTaskDone(Long taskId, ModemProviderSessionEntity providerSession) {
        TaskEntity task = taskEntityRepository
                .findAll(
                        TaskSpecification.withModemEntity()
                                .and(TaskSpecification.hasId(taskId))
                                .and(TaskSpecification.withServiceType())
                                .and(TaskSpecification.hasProviderSession_Id(providerSession.getId()))
                ).stream().findFirst()
                .orElse(null);
        if (task == null) return 0;
//                .findByIdAndModemProviderSessionEntity(taskId, providerSession);
        return setTaskDone(task, providerSession);
    }

    public int disconnectModemsFromProvider(List<ModemEntity> modems, ModemProviderSessionEntity modemProviderSession) {
        List<Long> ids = modems.stream()
                .map(ModemEntity::getId)
                .toList();
        return modemEntityRepository
                .updateModemProviderSessionEntityByIdInAndModemProviderSessionEntity(
                        null,
                        ids,
                        modemProviderSession
                );
    }

    public int disconnectModemOnProviderBusy(long taskId) {
        ModemEntity modemEntity = modemEntityRepository.findByTaskEntity_IdAndModemProviderSessionEntity_BusyTrue(taskId);
        if (modemEntity == null) {
            return 0;
        }
        return modemEntityRepository.updateModemProviderSessionEntityById(null, modemEntity.getId());
    }

    private int setTaskDone(TaskEntity task, ModemProviderSessionEntity providerSession) {
        if (task.isDone()) return 1;
        if (task.isReady() && task.isSuccess()) updateModemOnSuccess(task);
        return taskEntityRepository.
                updateDoneByIdAndModemProviderSessionEntity(true, task.getId(), providerSession);
    }

    public int setModemProviderBusy(ModemProviderSessionEntity modemProviderSession) {
        return modemProviderSessionRepo.
                updateBusyBySocketIdAndActiveTrue(true, modemProviderSession.getSocketId());
    }

    public int setModemsNotBusy(List<ModemEntity> modems) {
        return modemEntityRepository.
                updateBusyByPhoneNumberIn(
                        false,
                        modems.stream().map(ModemEntity::getPhoneNumber).collect(Collectors.toList()));
    }

    public ModemEntity getModemWithIMSI(String imsi) {
        return modemEntityRepository.findByIMSI(imsi);
    }

    public int setModemProviderNotBusy(ModemProviderSessionEntity modemProviderSession) {
        return modemProviderSessionRepo.
                updateBusyBySocketIdAndActiveTrue(false, modemProviderSession.getSocketId());
    }

    public ModemProviderSessionEntity createModemProviderSession(ModemProviderSessionEntity modemProviderSessionEntity, String token) throws ModemProviderSessionAlreadyActiveException {
        User user = userRepository.findFirstByEmail(tokenProvider.getUserName(token));
        modemProviderSessionEntity.setUser(user);
        if (!modemProviderSessionRepo.
                existsBySocketIdAndActiveTrue(modemProviderSessionEntity.getSocketId())) {
            return modemProviderSessionRepo.save(modemProviderSessionEntity);
        }
        throw new ModemProviderSessionAlreadyActiveException("Modem provider already active");
    }

    public int deactivateModemProvider(ModemProviderSessionEntity modemProviderSession) {
        modemEntityRepository.updateModemProviderSessionEntityByModemProviderSessionEntityNotNull(null);
        return modemProviderSessionRepo.
                updateActiveBySocketIdAndActiveTrue(false, modemProviderSession.getSocketId());
    }

    public List<ModemEntity> filterModems(List<ModemEntity> modems, List<ModemEntity> modemsFromDb) {
        Map<String, ModemEntity> phoneModems = new HashMap<>();
        for (ModemEntity modem : modemsFromDb) {
            phoneModems.put(modem.getIMSI(), modem);
        }
        List<ModemEntity> result = new ArrayList<>();
        for (ModemEntity modem : modems) {
            ModemEntity modemEntity = phoneModems.get(modem.getIMSI());
            if (modemEntity == null) continue;
            if (Objects.equals(modemEntity.getICCID(), modem.getICCID())) {
                result.add(modemEntity);
            }
        }
        return result;
    }

    public int updateModemOnProviderPort(Long taskId, String portName, ModemEntity modem, Map<String, WebSocketSession> sessions) {
        ModemProviderSessionEntity provider = modemProviderSessionRepo.
                findByModemEntityList_PhoneNumber(modem.getPhoneNumber());
        if (provider == null) return 0;
        WebSocketSession session = sessions.get(provider.getSocketId());
        if (session == null) return 0;
        UpdateModemOnPortContainer container = new UpdateModemOnPortContainer(
                taskId,
                Modem.toModel(modem),
                portName
        );
        Gson gson = new Gson();
        try {
            session.sendMessage(MessageFormer.formMessage(
                    OutCommands.UPDATE_PORT, gson.toJson(container)
            ));
            return 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int connectProviderToModems(List<ModemEntity> modems, ModemProviderSessionEntity modemProvider) {
        List<ModemEntity> existingModems = modemEntityRepository.
                findByModemProviderSessionEntityNullAndPhoneNumberIn(
                        modems.stream().map(ModemEntity::getPhoneNumber).collect(Collectors.toList()));
        return modemEntityRepository
                .updateModemProviderSessionEntityByPhoneNumberInAndModemProviderSessionEntityNull(
                        modemProvider,
                        filterModems(modems, existingModems).stream().map(ModemEntity::getPhoneNumber).collect(Collectors.toList()));
    }

    public int connectModemToProvider(ModemProviderSessionEntity providerSession, ModemEntity modem) {
        return modemEntityRepository.
                updateModemProviderSessionEntityAndBusyByPhoneNumber(
                        providerSession,
                        false,
                        modem.getPhoneNumber()
                );
    }

    public int disconnectModemsFromProvider(ModemProviderSessionEntity providerSession) {
        return modemEntityRepository.
                updateModemProviderSessionEntityByModemProviderSessionEntity(providerSession, null);
    }

    public int disconnectModemFromProvider(ModemEntity modem, ModemProviderSessionEntity providerSession) {
        return modemEntityRepository.
                updateModemProviderSessionEntityByModemProviderSessionEntityAndIMSI(
                        null,
                        providerSession,
                        modem.getIMSI());
    }

    public ModemEntity getModemWithTaskId(Long taskId) {
        return modemEntityRepository.findByTaskEntity_Id(taskId);
    }

    public List<ModemEntity> saveNewModems(List<ModemEntity> modems) {
        List<String> existingNumbers = modemEntityRepository.findByPhoneNumberIn(
                modems.stream().map(ModemEntity::getPhoneNumber).collect(Collectors.toList())
        ).stream().map(ModemEntity::getPhoneNumber).toList();
        List<ModemEntity> newModems = (List<ModemEntity>) modemEntityRepository.saveAll(modems.stream().filter(
                m -> !existingNumbers.contains(m.getPhoneNumber())).collect(Collectors.toList()
        ));
        createNewUsedServices(newModems);
        return newModems;
    }

    private List<UsedServiceTypeEntity> createNewUsedServices(List<ModemEntity> newModems) {
        List<ServiceTypeEntity> services = (List<ServiceTypeEntity>) serviceTypeEntityRepository.findAll();
        List<UsedServiceTypeEntity> resultUsedServices = new ArrayList<>();
        for (ModemEntity modem : newModems) {
            for (ServiceTypeEntity serviceType : services) {
                UsedServiceTypeEntity newUsedServiceType = new UsedServiceTypeEntity();
                newUsedServiceType.setServiceType(serviceType);
                newUsedServiceType.setModemEntity(modem);
                resultUsedServices.add(newUsedServiceType);
            }
        }
        return (List<UsedServiceTypeEntity>) usedServiceTypeEntityRepository.saveAll(resultUsedServices);
    }

    public ModemEntity getModemWithIMSIAndICCID(ModemEntity modem) {
        if (modem.getIMSI() == null || modem.getICCID() == null) return null;
        return modemEntityRepository.findByIMSIAndICCID(modem.getIMSI(), modem.getICCID());
    }

    public int setModemBusy(ModemEntity modem, ModemProviderSessionEntity modemProviderSession) {
        return modemEntityRepository.
                updateBusyByPhoneNumberAndModemProviderSessionEntity(
                        true, modem.getPhoneNumber(), modemProviderSession);
    }

    public int setModemNotBusy(ModemEntity modem, ModemProviderSessionEntity modemProviderSession) {
        return modemEntityRepository.
                updateBusyByPhoneNumberAndModemProviderSessionEntity(
                        false, modem.getPhoneNumber(), modemProviderSession);
    }

    public ModemEntity updateModemOnBusyTask(Long taskId, Map<String, WebSocketSession> sessions, ModemProviderSessionEntity providerSession) {
        if (taskService.checkIfReserved(taskId)) return null;
        ModemEntity chosenModem = null;
        try {
            chosenModem = taskService.getAvailableModem(
                    taskEntityRepository.findById(taskId).get().getServiceTypeEntity().getAbbreviation()
            );
        } catch (CouldNotFindSuchModemException e) {
            setTaskNotReady(taskId);
            setTaskDone(taskId, providerSession);
            return null;
        }
        ModemProviderSessionEntity newModemProvider = modemProviderSessionRepo.
                findByModemEntityList_Id(chosenModem.getId());
        taskEntityRepository.
                updateModemEntityAndModemProviderSessionEntityById(
                        chosenModem,
                        newModemProvider,
                        taskId);
        WebSocketSession newModemProviderSession = sessions.get(newModemProvider.getSocketId());
        Gson gson = new Gson();
        ModemCheckOutContainer modemCheckContainer = new ModemCheckOutContainer(taskId, Modem.toModel(chosenModem));
        try {
            newModemProviderSession.
                    sendMessage(MessageFormer.formMessage(OutCommands.CHECK_MODEM, gson.toJson(modemCheckContainer)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chosenModem;
    }

    public int filterMessages(
            Long taskId,
            ModemProviderSessionEntity providerSession,
            List<MessageEntity> messages) {
        TaskEntity task = taskEntityRepository.
                findByIdAndModemProviderSessionEntity_Id(taskId, providerSession.getId());
        ServiceTypeEntity service = serviceTypeEntityRepository.findByTaskEntity_Id(taskId);
        if (service == null || task == null) return 0;
        Pattern messageRegex = Pattern.compile(service.getMessageRegex());
        Pattern senderRegex = Pattern.compile(service.getSenderRegex());
        for (MessageEntity message : messages) {
            Matcher messageMatcher = messageRegex.matcher(message.getMessage());
            if (!messageMatcher.find()) continue;
            Matcher senderMatcher = senderRegex.matcher(message.getSender());
            if (!senderMatcher.find()) continue;
            message.setTaskEntity(task);
            setTaskSuccess(taskId);
            setTaskDone(task, providerSession);
            messageEntityRepository.save(message);
        }
        return 1;
    }

    public int setTaskSuccess(Long taskId) {
        return taskEntityRepository.updateSuccessById(true, taskId);
    }


    public List<Modem> handleBlankModems(List<ModemEntity> blankModems) {
        List<ModemEntity> modems = modemEntityRepository.
                findByIMSIIn(blankModems.stream().map(ModemEntity::getIMSI).collect(Collectors.toList()));
        Map<String, ModemEntity> imsiModems = new HashMap<>();
        for (ModemEntity modem : modems) {
            imsiModems.put(modem.getIMSI(), modem);
        }
        List<Modem> result = new ArrayList<>();
        for (ModemEntity modem : blankModems) {
            ModemEntity trueModem = imsiModems.get(modem.getIMSI());
            if (trueModem == null) {
                continue;
            }
            if (Objects.equals(trueModem.getICCID(), modem.getICCID())) {
                result.add(Modem.toModel(trueModem));
            }
        }
        return result;
    }

    public boolean checkIfModemMatch(Long taskId, ModemEntity modem) {
        if (modem == null) return false;
        return taskEntityRepository.
                existsByIdAndModemEntity_IMSIAndModemEntity_ICCID(taskId, modem.getIMSI(), modem.getICCID());
    }

    public int setTaskReady(Long taskId) {
        return taskEntityRepository.updateReadyById(true, taskId);
    }

    public int setTaskNotReady(Long taskId) {
        return taskEntityRepository.updateReadyById(false, taskId);
    }

    public int disconnectNotBusyModems(ModemProviderSessionEntity providerSession) {
        return modemEntityRepository
                .updateModemProviderSessionEntityByModemProviderSessionEntityAndBusyFalse(
                        null, providerSession);
    }

    public void checkProvider(String nameCausePort, Long taskId, ModemProviderSessionEntity providerSession, WebSocketSession socketSession) {
        modemProviderSessionRepo.
                updateBusyByActiveTrueAndSocketId(true, providerSession.getSocketId());
        Gson gson = new Gson();
        CheckProviderOutContainer container = new CheckProviderOutContainer(taskId, nameCausePort);
        try {
            socketSession.sendMessage(MessageFormer.formMessage(
                    OutCommands.UPDATE_PROVIDER, gson.toJson(container)
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkProvider(String nameCausePort, Long taskId, ModemProviderSessionEntity providerSession, Map<String, WebSocketSession> sessions) {
        WebSocketSession session = sessions.get(providerSession.getSocketId());
        checkProvider(nameCausePort, taskId, providerSession, session);
    }
}
