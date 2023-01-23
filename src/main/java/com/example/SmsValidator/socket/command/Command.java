package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public abstract class Command {
    protected String json;
    protected ModemProviderSessionEntity modemProviderSession;
    protected SocketService service;
    protected WebSocketSession session;
    protected Map<String, WebSocketSession> sessions;

    public Command(String json,
                   ModemProviderSessionEntity modemProviderSession,
                   SocketService service,
                   WebSocketSession session,
                   Map<String, WebSocketSession> sessions) {
        this.json = json;
        this.modemProviderSession = modemProviderSession;
        this.service = service;
        this.session = session;
        this.sessions = sessions;
    }

    public abstract void run();
}
