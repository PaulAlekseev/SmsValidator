package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.model.Modem;

public class ModemCheckOutContainer {
    private Long taskId;
    private Modem modem;

    public ModemCheckOutContainer(Long taskId, Modem modem) {
        this.taskId = taskId;
        this.modem = modem;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Modem getModem() {
        return modem;
    }

    public void setModem(Modem modem) {
        this.modem = modem;
    }
}
