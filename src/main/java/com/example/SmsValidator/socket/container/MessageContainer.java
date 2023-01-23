package com.example.SmsValidator.socket.container;

import com.example.SmsValidator.entity.MessageEntity;

import java.util.List;

public class MessageContainer {
    private Long taskId;
    private Long timeSeconds;
    private List<MessageEntity> messages;

    public MessageContainer(Long taskId, Long timeSeconds, List<MessageEntity> messages) {
        this.taskId = taskId;
        this.messages = messages;
        this.timeSeconds = timeSeconds;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public Long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }
}
