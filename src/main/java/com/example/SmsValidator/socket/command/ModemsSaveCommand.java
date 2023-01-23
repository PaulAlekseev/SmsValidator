package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.OutCommands;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModemsSaveCommand extends Command{
    public ModemsSaveCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<ModemEntity>>(){}.getType();
        List<ModemEntity> list =  gson.fromJson(json, token);
        try {
            session.sendMessage(MessageFormer.
                    formMessage(OutCommands.SAVED_MODEMS, gson.toJson(service.saveNewModems(list))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
