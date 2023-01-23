package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.model.Modem;

public class UpdateProviderContainer {
    private Long taskId;
    private String portName;
    private Modem modem;

    public UpdateProviderContainer(Long taskId, String portName, Modem modem) {
        this.taskId = taskId;
        this.portName = portName;
        this.modem = modem;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Modem getModem() {
        return modem;
    }

    public void setModem(Modem modem) {
        this.modem = modem;
    }
}
