package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.entity.TaskEntity;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.container.MessageOutContainer;
import com.example.SmsValidator.socket.container.ModemCheckContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

public class ModemCheckCommand extends Command{
    public ModemCheckCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        ModemCheckContainer container = gson.fromJson(json, ModemCheckContainer.class);
        if (!service.checkIfModemMatch(container.getTaskId(), container.getModem())) {
            service.disconnectNotBusyModems(modemProviderSession);
            service.checkProvider(container.getPortName(), container.getTaskId(), modemProviderSession, sessions);
            service.updateModemOnBusyTask(container.getTaskId(), sessions, modemProviderSession);
            return;
        }
        if (container.getSignalQuality() < 5) {
            service.updateModemOnBusyTask(container.getTaskId(), sessions, modemProviderSession);
            return;
        }
        service.setTaskReady(container.getTaskId());
        TaskEntity task = service.getTaskById(container.getTaskId());
        MessageOutContainer messageContainer = new MessageOutContainer(
                container.getTaskId(),
                task.getTimeSeconds(),
                Modem.toModel(service.getModemWithIMSIAndICCID(container.getModem()))
        );
        try {
            session.sendMessage(
                    MessageFormer.formMessage(
                            OutCommands.MESSAGE,
                            gson.toJson(messageContainer)
                            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
