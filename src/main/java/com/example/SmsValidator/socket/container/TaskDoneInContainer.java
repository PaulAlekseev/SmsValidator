package com.example.SmsValidator.socket.container;

public class TaskDoneInContainer {
    private final Long taskId;

    public TaskDoneInContainer(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}
