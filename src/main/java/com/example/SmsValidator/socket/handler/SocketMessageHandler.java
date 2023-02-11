package com.example.SmsValidator.socket.handler;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.exception.customexceptions.socket.CouldNotParseSocketMessageException;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.SocketMessage;
import com.example.SmsValidator.socket.command.Command;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class SocketMessageHandler {

    public static void handleMessage(
            String message,
            ModemProviderSessionEntity providerSessionEntity,
            SocketService service,
            WebSocketSession session,
            Map<String, WebSocketSession> sessions) throws CouldNotParseSocketMessageException {
//        try {
            SocketMessage socketMessage = SocketMessageParser.parseMessage(message);
            Command command = DecideTaskHandler.
                    decideTask(socketMessage,
                            providerSessionEntity,
                            service,
                            session,
                            sessions);
            if (command == null) {
                return;
            }
            command.run();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}
