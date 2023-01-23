package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.ModemService;
import com.example.SmsValidator.service.SocketService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class ModemSetBusyCommand extends Command{
    public ModemSetBusyCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        ModemEntity modem = gson.fromJson(json, ModemEntity.class);
        service.setModemBusy(modem, modemProviderSession);
    }
}
