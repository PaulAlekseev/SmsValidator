package com.example.SmsValidator.model;

import com.example.SmsValidator.entity.MessageEntity;

public class Message {
    private String sender;

    private String message;

    private Message() {
    }

    public static Message toModel(MessageEntity messageEntity) {
        Message message = new Message();
        message.setMessage(messageEntity.getMessage());
        message.setSender(messageEntity.getSender());
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
