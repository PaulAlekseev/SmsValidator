package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.entity.ModemEntity;

public class ConnectModemContainer {
    private String taskId;
    private ModemEntity modem;

    public ConnectModemContainer(String taskId, ModemEntity modem) {
        this.taskId = taskId;
        this.modem = modem;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ModemEntity getModem() {
        return modem;
    }

    public void setModem(ModemEntity modem) {
        this.modem = modem;
    }
}
