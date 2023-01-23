package com.example.SmsValidator.model;

import com.example.SmsValidator.entity.TaskEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Task {
    private Long id;
    private String phoneNumber;
    private boolean ready;
    private boolean done;
    private Long timeSeconds;
    private List<Message> messages;

    private Task() {
    }

    public static Task toModel(TaskEntity task) {
        Task taskModel = new Task();
        taskModel.setId(task.getId());
        taskModel.setTimeSeconds(task.getTimeSeconds());
        taskModel.setReady(task.isReady());
        taskModel.setDone(task.isDone());
        taskModel.setPhoneNumber(task.getModemEntity().getPhoneNumber());
        taskModel.setMessages(
                task.getMessages().stream().map(Message::toModel).collect(Collectors.toList()));
        return taskModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
