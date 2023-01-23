package com.example.SmsValidator.socket;

import org.springframework.web.socket.TextMessage;

public class MessageFormer {
    public static TextMessage formMessage(String command, String json) {
        return new TextMessage(command + ":" + json);
    }
}
