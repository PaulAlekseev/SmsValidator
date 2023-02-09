package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.provider.ProviderActivateBaseResponse;
import com.example.SmsValidator.bean.provider.ProviderActivateErrorResponse;
import com.example.SmsValidator.bean.provider.ProviderActivateSuccessResponse;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.ModemProviderSessionEntityRepository;
import com.example.SmsValidator.socket.MessageFormer;
import com.example.SmsValidator.socket.ModemSocketHandler;
import com.example.SmsValidator.socket.OutCommands;
import com.example.SmsValidator.socket.container.ProviderReadyOutContainer;
import com.example.SmsValidator.socket.container.ProviderStopOutContainer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final Gson gson = new Gson();

    private final ModemSocketHandler modemSocketHandler;
    private final SocketService socketService;
    private final ModemEntityRepository modemEntityRepository;
    private final ModemProviderSessionEntityRepository modemProviderSessionEntityRepository;

    public ProviderActivateBaseResponse activate() {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(SecurityContextHolder.getContext());
        if (modemProviderSession == null) return new ProviderActivateErrorResponse("No such session");
        if (!modemProviderSession.getBusy()) return new ProviderActivateErrorResponse("Provider already active");
        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.START_PROVIDER, gson.toJson(new ProviderReadyOutContainer())));
        } catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    public ProviderActivateBaseResponse stop() {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(SecurityContextHolder.getContext());
        if (modemProviderSession == null) return new ProviderActivateErrorResponse("No such session");
        if (modemProviderSession.getBusy()) return new ProviderActivateErrorResponse("Provider already stopped");
        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
        socketService.disconnectNotBusyModems(modemProviderSession);
        socketService.setModemProviderBusy(modemProviderSession);
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.STOP_PROVIDER, gson.toJson(new ProviderStopOutContainer())));
        }catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    public ProviderActivateBaseResponse saveModems() {
        ModemProviderSessionEntity modemProviderSession = getProviderSessionFromContext(SecurityContextHolder.getContext());
        if (modemProviderSession == null) return new ProviderActivateErrorResponse("No such session");
        if (!modemProviderSession.getBusy()) return new ProviderActivateErrorResponse("Provider is active right now");
        WebSocketSession session = modemSocketHandler.getSessions().get(modemProviderSession.getSocketId());
        try {
            session.sendMessage(MessageFormer.formMessage(OutCommands.START_SAVE_MODEMS, gson.toJson(new ProviderReadyOutContainer())));
            modemEntityRepository.
                    updateModemProviderSessionEntityByModemProviderSessionEntityAndBusyFalse(null, modemProviderSession);
        }catch (IOException e) {
            return new ProviderActivateErrorResponse(e.getLocalizedMessage());
        }
        return new ProviderActivateSuccessResponse();
    }

    private ModemProviderSessionEntity getProviderSessionFromContext(SecurityContext contextHolder) {
        Authentication authentication = contextHolder.getAuthentication();
        ModemProviderSessionEntity sessionEntity = modemProviderSessionEntityRepository
                .findByUser_EmailAndActiveTrue(authentication.getName());
        return sessionEntity;
    }
}
