package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class ProviderNotReadyCommand extends Command{

    public ProviderNotReadyCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        service.setModemProviderBusy(modemProviderSession);
    }
}
