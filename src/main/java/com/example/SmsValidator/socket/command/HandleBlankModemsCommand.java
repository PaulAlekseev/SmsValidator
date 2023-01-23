package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.model.ModemState;
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
import java.util.stream.Collectors;

public class HandleBlankModemsCommand extends Command{
    public HandleBlankModemsCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        Type token = new TypeToken<ArrayList<ModemEntity>>(){}.getType();
        List<ModemEntity> list =  gson.fromJson(json, token);
        List<Modem> result =  service.handleBlankModems(list);
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.SET_STATIONARY_MODEMS, gson.toJson(result)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
