package com.example.SmsValidator.service;

import com.example.SmsValidator.entity.*;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.model.Task;
import com.example.SmsValidator.repository.*;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.SocketConfiguration;
import com.example.SmsValidator.socket.container.ModemCheckOutContainer;
import com.example.SmsValidator.socket.container.ModemMessageOutContainer;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Service
public class TaskService {
    @Autowired
    private TaskEntityRepository taskRepo;
    @Autowired
    private ModemEntityRepository modemEntityRepository;
    @Autowired
    private SocketConfiguration socketConfiguration;
    @Autowired
    private ModemProviderSessionEntityRepository providerSessionRepository;
    @Autowired
    private ServiceTypeEntityRepository serviceRepository;
    @Autowired
    private UsedServiceTypeEntityRepository userServiceRepository;

    public ModemEntity getAvailableModem(ServiceTypeEntity serviceType) {
        LocalDate currentDate = LocalDate.now();
        Date newDate = Date.from(currentDate.minusDays(serviceType.getDaysBetween()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        UsedServiceTypeEntity usedServiceTypeEntity = userServiceRepository.
                findFirstByServiceType_IdAndTimesUsedLessThanAndLastTimeUsedLessThanEqualAndModemEntity_BusyFalseOrderByTimesUsedDesc(
                        serviceType.getId(),
                        serviceType.getAllowedAmount(),
                        newDate
                );
        return usedServiceTypeEntity != null ?
                usedServiceTypeEntity.getModemEntity() :
                modemEntityRepository.
                findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityListEmptyOrderByIdAsc();
    }

    public ModemEntity getAvailableModem(Long serviceEntityId) {
        return getAvailableModem(serviceRepository.findById(serviceEntityId).get());
    }

    public Task createTask(Long serviceId, Long timeSeconds) {
        TaskEntity task = new TaskEntity();
        ServiceTypeEntity serviceType = serviceRepository.findById(serviceId).get();
        task.setServiceTypeEntity(serviceType);
        ModemEntity chosenModem = getAvailableModem(serviceType);
        if (chosenModem == null) return null;
        ModemProviderSessionEntity chosenProvider = providerSessionRepository.findByModemEntityList_Id(chosenModem.getId());
        task.setModemEntity(chosenModem);
        task.setModemProviderSessionEntity(chosenProvider);
        task.setTimeSeconds(timeSeconds);
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
        return Task.toModel(resultTask);
    }

    public ModemEntity startMessages(Long taskId, int seconds) {
        TaskEntity task = taskRepo.findById(taskId).get();
        WebSocketSession chosenSession = socketConfiguration.
                getSocketHandler().
                getSessions().
                get(providerSessionRepository.findByTaskEntityList_Id(taskId).getSocketId());
        Gson gson = new Gson();
        try {
            chosenSession.
                    sendMessage(MessageFormer.
                            formMessage(OutCommands.MESSAGE, gson.toJson(new ModemMessageOutContainer(taskId, Modem.toModel(task.getModemEntity()), seconds))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return task.getModemEntity();
    }

    public Task getTask(Long taskId) {
        return Task.toModel(taskRepo.findById(taskId).get());
    }
}