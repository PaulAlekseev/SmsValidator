package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProviderReadyCommand extends Command{
    public ProviderReadyCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        service.setModemProviderNotBusy(modemProviderSession);
        Type token = new TypeToken<ArrayList<ModemEntity>>(){}.getType();
        List<ModemEntity> list =  gson.fromJson(json, token);
        service.connectProviderToModems(list, modemProviderSession);
        service.setModemsNotBusy(list);
    }
}