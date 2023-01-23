package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.model.Modem;

public class UpdateModemOnPortContainer {
    private final Long taskId;
    private final Modem modem;
    private final String portName;

    public UpdateModemOnPortContainer(Long taskId, Modem modem, String portName) {
        this.taskId = taskId;
        this.modem = modem;
        this.portName = portName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Modem getModem() {
        return modem;
    }

    public String getPortName() {
        return portName;
    }
}
