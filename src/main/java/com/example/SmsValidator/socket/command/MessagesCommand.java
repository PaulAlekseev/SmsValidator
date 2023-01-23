package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.container.MessageContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class MessagesCommand extends Command{
    public MessagesCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        MessageContainer container = gson.fromJson(json, MessageContainer.class);
        service.filterMessages(container.getTaskId(), modemProviderSession, container.getMessages());
    }
}
