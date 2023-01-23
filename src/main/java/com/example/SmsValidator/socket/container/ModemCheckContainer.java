package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.entity.ModemEntity;

public class ModemCheckContainer {
    private final Long taskId;
    private final int signalQuality;
    private final ModemEntity modem;
    private final String portName;

    public ModemCheckContainer(Long taskId, int signalQuality, ModemEntity modem, String portName) {
        this.taskId = taskId;
        this.signalQuality = signalQuality;
        this.modem = modem;
        this.portName = portName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public int getSignalQuality() {
        return signalQuality;
    }

    public ModemEntity getModem() {
        return modem;
    }

    public String getPortName() {
        return portName;
    }
}
