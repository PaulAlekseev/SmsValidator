package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.model.Modem;

public class ModemMessageOutContainer {
    private long taskId;
    private Modem modemState;
    private long timeSeconds;

    public ModemMessageOutContainer(long taskId, Modem modemState, long timeSeconds) {
        this.taskId = taskId;
        this.modemState = modemState;
        this.timeSeconds = timeSeconds;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Modem getModemState() {
        return modemState;
    }

    public void setModemState(Modem modemState) {
        this.modemState = modemState;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }
}
