package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.container.ModemBusyContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class ModemBusyCommand extends Command{

    public ModemBusyCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        ModemBusyContainer modemContainer = gson.fromJson(json, ModemBusyContainer.class);
        service.updateModemOnBusyTask(modemContainer.getTaskId(), sessions, modemProviderSession);
    }
}
