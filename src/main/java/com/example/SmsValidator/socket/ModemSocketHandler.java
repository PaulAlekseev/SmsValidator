package com.example.SmsValidator.socket;

import com.example.SmsValidator.auth.JwtTokenProvider;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.repository.ModemProviderSessionEntityRepository;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.handler.SocketMessageHandler;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModemSocketHandler implements WebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Autowired
    private SocketService service;
    private JwtTokenProvider tokenProvider;
    @Autowired
    private ModemProviderSessionEntityRepository providerSessionRepo;

    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ModemProviderSessionEntity providerSession = new ModemProviderSessionEntity();
        providerSession.setSocketId(session.getId());
        providerSession.setActive(true);
        providerSession.setBusy(true);
        Pattern pattern = Pattern.compile("Bearer\s(.*)");
        Matcher matcher = pattern.matcher(session.getHandshakeHeaders().get("authorization").get(0));
        if(!matcher.find()) {
            session.close();
            return;
        }
        String token = matcher.group(1);

        service.createModemProviderSession(providerSession, token);
        sessions.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(message.getPayload().toString());
        SocketMessageHandler
                .handleMessage(
                        message.getPayload().toString(),
                        providerSessionRepo.findBySocketIdAndActiveTrue(session.getId()),
                        service,
                        session,
                        getSessions());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        ModemProviderSessionEntity providerSession = providerSessionRepo.findBySocketIdAndActiveTrue(session.getId());
        service.disconnectModemsFromProvider(providerSession);
        service.deactivateModemProvider(providerSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
