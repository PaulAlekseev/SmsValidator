package com.example.SmsValidator.socket.container;

public class CheckProviderOutContainer {
    private final Long taskId;
    private final String portName;

    public CheckProviderOutContainer(Long taskId, String portName) {
        this.taskId = taskId;
        this.portName = portName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getPortName() {
        return portName;
    }
}
