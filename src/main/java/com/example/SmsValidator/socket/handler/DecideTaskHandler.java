package com.example.SmsValidator.socket.handler;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.SocketMessage;
import com.example.SmsValidator.socket.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class DecideTaskHandler {

    public static Command decideTask(SocketMessage message,
                                     ModemProviderSessionEntity providerSession,
                                     SocketService service,
                                     WebSocketSession session,
                                     Map<String, WebSocketSession> sessions) {
        return switch (message.getCommand()) {
            case "SaveModems" -> new ModemsSaveCommand(message.getJson(), providerSession, service, session, sessions);
            case "ProviderBusy" -> new ProviderNotReadyCommand(message.getJson(), providerSession, service, session, sessions);
            case "ProviderReady" -> new ProviderReadyCommand(message.getJson(), providerSession, service, session, sessions);
            case "ModemSetBusy" -> new ModemSetBusyCommand(message.getJson(), providerSession, service, session, sessions);
            case "ModemSetReady" -> new ModemSetNotBusyCommand(message.getJson(), providerSession, service, session, sessions);
            case "ModemBusy" -> new ModemBusyCommand(message.getJson(), providerSession, service, session, sessions);
            case "ModemCheck" -> new ModemCheckCommand(message.getJson(), providerSession, service, session, sessions);
            case "HandleBlankModems" -> new HandleBlankModemsCommand(message.getJson(), providerSession, service, session, sessions);
            case "ConnectModem" -> new ConnectModemCommand(message.getJson(), providerSession, service, session, sessions);
            case "Messages" -> new MessagesCommand(message.getJson(), providerSession, service, session, sessions);
            case "TaskSetDone" -> new TaskDoneCommand(message.getJson(), providerSession, service, session, sessions);
            default -> null;
        };
    }
}
