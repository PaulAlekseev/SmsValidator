package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.entity.ModemEntity;

public class ModemBusyContainer {
    private Long taskId;
    private ModemEntity modem;

    public ModemBusyContainer(Long taskId, ModemEntity modem) {
        this.taskId = taskId;
        this.modem = modem;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public ModemEntity getModem() {
        return modem;
    }

    public void setModem(ModemEntity modem) {
        this.modem = modem;
    }
}
