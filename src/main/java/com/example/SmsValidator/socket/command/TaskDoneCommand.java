package com.example.SmsValidator.socket.command;

import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.service.SocketService;
import com.example.SmsValidator.socket.container.TaskDoneInContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class TaskDoneCommand extends Command{
    public TaskDoneCommand(String json, ModemProviderSessionEntity modemProviderSession, SocketService service, WebSocketSession session, Map<String, WebSocketSession> sessions) {
        super(json, modemProviderSession, service, session, sessions);
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        TaskDoneInContainer container = gson.fromJson(json, TaskDoneInContainer.class);
        service.setTaskDone(container.getTaskId(), modemProviderSession);
        service.disconnectModemOnProviderBusy(container.getTaskId());
    }
}
