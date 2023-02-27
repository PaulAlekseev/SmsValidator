package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.container.DisconnectModemsInContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class DisconnectModemsCommand extends Command {
    public DisconnectModemsCommand(String json, ModemProviderSessionEntity providerSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, providerSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        DisconnectModemsInContainer container = gson.fromJson(json, DisconnectModemsInContainer.class);
        service.disconnectModemsFromProvider(container.getModems(), modemProviderSession);
    }
}
