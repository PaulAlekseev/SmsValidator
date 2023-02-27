package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.task.TaskBaseResponse;
import com.example.SmsValidator.bean.task.TaskErrorResponse;
import com.example.SmsValidator.bean.task.TaskSuccessResponse;
import com.example.SmsValidator.config.SocketConfiguration;
import com.example.SmsValidator.entity.*;
import com.example.SmsValidator.exception.CustomException;
import com.example.SmsValidator.exception.customexceptions.provider.CouldNotFindSuchModemException;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.model.Task;
import com.example.SmsValidator.repository.*;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.container.ModemCheckOutContainer;
import com.example.SmsValidator.specification.ModemSpecification;
import com.example.SmsValidator.specification.extra.Reserved;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskEntityRepository taskRepo;
    private final ModemEntityRepository modemEntityRepository;
    private final SocketConfiguration socketConfiguration;
    private final ModemProviderSessionEntityRepository providerSessionRepository;
    private final ServiceTypeEntityRepository serviceRepository;
    private final UsedServiceTypeEntityRepository usedServiceRepository;
    private final UserRepository userRepository;

    public boolean checkIfReserved(Long taskId) {
        return taskRepo.existsByIdAndReservedTrue(taskId);
    }

    public ModemEntity getAvailableModem(String serviceAbbreviation) throws CouldNotFindSuchModemException {
        return modemEntityRepository.findAll(
                        ModemSpecification.notUsedService(serviceAbbreviation)
                                .and(ModemSpecification.hasModemProviderSessionEntity_Busy(false))
                                .and(ModemSpecification.hasModemProviderSessionEntity_Active(true))
                                .and(ModemSpecification.hasBusy(false))
                                .and(ModemSpecification.isReserved(Reserved.NOT_RESERVED))
                                .and(ModemSpecification.withModemProviderSession())
                ).stream().findFirst()
                .orElseThrow(() -> new CouldNotFindSuchModemException("Could not find such modem"));
    }

    public int getAmountOfAvailableServices(String serviceAbbreviation) {
        return (int) modemEntityRepository.count(
                ModemSpecification.notUsedService(serviceAbbreviation)
                        .and(ModemSpecification.hasModemProviderSessionEntity_Busy(false))
                        .and(ModemSpecification.hasModemProviderSessionEntity_Active(true))
                        .and(ModemSpecification.hasBusy(false))
                        .and(ModemSpecification.isReserved(Reserved.NOT_RESERVED))
        );
    }


//                .findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_ReservedUntilLessThanAndModemEntity_BusyFalseOrderByTimesUsedDesc(
//                        serviceType.getId(),
//                        serviceType.getAllowedAmount(),
//                        newDate,
//                        Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
//                );
//        return usedServiceTypeEntity != null ?
//                usedServiceTypeEntity.getModemEntity() :
//                modemEntityRepository
//                        .findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityList_ServiceType_IdNotAndReservedUntilLessThanOrderByIdDesc(serviceType.getId(), new Date());
//                        .findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityListEmptyAndReservedUntilLessThanOrderByIdDesc(new Date());
//    }

    public TaskBaseResponse createTelegramTask(Long serviceId, Long telegramId) throws CustomException, IOException {
        User user = userRepository.findFirstByTelegramId(telegramId);
        if (user == null) return new TaskErrorResponse("Could not find user with teleId");
        return createTask(serviceId, user);
    }

    public TaskBaseResponse createReservedTelegramTask(Long serviceId, Long modemId, Long telegramId) {
        User user = userRepository.findFirstByTelegramId(telegramId);
        ModemEntity modem = modemEntityRepository.findFirstById(modemId);
        if (user == null) return new TaskErrorResponse("Could not find user with teleId");
        return createReservedTask(serviceId, modem, user);
    }

    public TaskBaseResponse createReservedTask(Long serviceId, Long modemId, Principal principal) {
        ModemEntity modem = modemEntityRepository.findFirstById(modemId);
        if (modem == null) return new TaskErrorResponse("Could not found such modem");
        if (modem.getBusy()) return new TaskErrorResponse("Modem is busy right now");
        User user = userRepository.findFirstByEmail(principal.getName());
        return createReservedTask(serviceId, modem, user);
    }

    public TaskBaseResponse createReservedTask(Long serviceId, ModemEntity chosenModem, User user) {
        TaskEntity task = new TaskEntity();
        ServiceTypeEntity serviceType = serviceRepository.findById(serviceId).get();
        task.setServiceTypeEntity(serviceType);
        ModemProviderSessionEntity chosenProvider = providerSessionRepository.findByModemEntityList_Id(chosenModem.getId());
        task.setModemEntity(chosenModem);
        task.setModemProviderSessionEntity(chosenProvider);
        task.setTimeSeconds(serviceType.getTimeSeconds());
        task.setUser(user);
        task.setReserved(true);
        WebSocketSession chosenSession = socketConfiguration.
                getSocketHandler().
                getSessions().
                get(chosenProvider.getSocketId());
        TaskEntity resultTask = taskRepo.save(task);
        resultTask.setMessages(new ArrayList<>());
        ModemCheckOutContainer container = new ModemCheckOutContainer(resultTask.getId(), Modem.toModel(chosenModem));
        Gson gson = new Gson();
        try {
            chosenSession.sendMessage(MessageFormer.formMessage(OutCommands.CHECK_MODEM, gson.toJson(container)));
            modemEntityRepository.
                    updateBusyByPhoneNumberAndModemProviderSessionEntity(
                            true, chosenModem.getPhoneNumber(), chosenProvider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new TaskSuccessResponse(
                Modem.toModel(chosenModem),
                Task.toModel(resultTask)
        );
    }

    public TaskBaseResponse createTask(Long serviceId, Principal principal) throws CustomException, IOException {
        return createTask(serviceId, userRepository.findFirstByEmail(principal.getName()));
    }

    public TaskBaseResponse createTask(Long serviceId, User user) throws CustomException, IOException {
            TaskEntity task = new TaskEntity();
            ServiceTypeEntity serviceType = serviceRepository
                    .findById(serviceId)
                    .orElseThrow(() -> new CustomException("Could not find such Service"));
            task.setServiceTypeEntity(serviceType);
            ModemEntity chosenModem = getAvailableModem(serviceType.getAbbreviation());
            ModemProviderSessionEntity chosenProvider = providerSessionRepository.findByModemEntityList_Id(chosenModem.getId());
            task.setModemEntity(chosenModem);
            task.setModemProviderSessionEntity(chosenProvider);
            task.setTimeSeconds(serviceType.getTimeSeconds());
            task.setUser(user);
            task.setReserved(false);
            WebSocketSession chosenSession = socketConfiguration.
                    getSocketHandler().
                    getSessions().
                    get(chosenProvider.getSocketId());
            TaskEntity resultTask = taskRepo.save(task);
            resultTask.setMessages(new ArrayList<>());

            ModemCheckOutContainer container = new ModemCheckOutContainer(resultTask.getId(), Modem.toModel(chosenModem));
            Gson gson = new Gson();
            chosenSession.sendMessage(MessageFormer.formMessage(OutCommands.CHECK_MODEM, gson.toJson(container)));
            modemEntityRepository.
                    updateBusyByPhoneNumberAndModemProviderSessionEntity(
                            true, chosenModem.getPhoneNumber(), chosenProvider);
            return new TaskSuccessResponse(Modem.toModel(chosenModem), Task.toModel(task));
    }


//    public ModemEntity startMessages(Long taskId, int seconds) {
//        TaskEntity task = taskRepo.findById(taskId).get();
//        WebSocketSession chosenSession = socketConfiguration.
//                getSocketHandler().
//                getSessions().
//                get(providerSessionRepository.findByTaskEntityList_Id(taskId).getSocketId());
//        Gson gson = new Gson();
//        try {
//            chosenSession.
//                    sendMessage(MessageFormer.
//                            formMessage(OutCommands.MESSAGE, gson.toJson(new ModemMessageOutContainer(taskId, Modem.toModel(task.getModemEntity()), seconds))));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return task.getModemEntity();
//    }


    public TaskBaseResponse getTask(Long taskId) {
        TaskEntity taskEntity = taskRepo.findFirstById(taskId);
        if (taskEntity == null) return new TaskErrorResponse("Task does not exist");
        ModemEntity modemEntity = taskEntity.getModemEntity();
        Task task = Task.toModel(taskEntity);
        Modem modem = Modem.toModel(taskEntity.getModemEntity());
        return modemEntity == null ?
                new TaskErrorResponse("Task does not exist") :
                new TaskSuccessResponse(Modem.toClientModem(modemEntity), Task.toModel(taskEntity));
    }
}
