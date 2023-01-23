package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.model.Modem;

public class MessageOutContainer {
    private Long taskId;
    private Long timeSeconds;
    private Modem modem;

    public MessageOutContainer(Long taskId, Long timeSeconds, Modem modem) {
        this.taskId = taskId;
        this.timeSeconds = timeSeconds;
        this.modem = modem;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public Modem getModem() {
        return modem;
    }

    public void setModem(Modem modem) {
        this.modem = modem;
    }
}
