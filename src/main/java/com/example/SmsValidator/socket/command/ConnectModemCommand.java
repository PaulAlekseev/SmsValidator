package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.container.ConnectModemContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class ConnectModemCommand extends Command{

    public ConnectModemCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        ConnectModemContainer container = gson.fromJson(json, ConnectModemContainer.class);
        if (container.getModem() == null) return;
        ModemEntity modem = service.getModemWithIMSIAndICCID(container.getModem());
        service.connectModemToProvider(
                modemProviderSession,
                modem);
    }
}
