package com.example.SmsValidator.socket;

public class SocketMessage {
    private final String command;
    private final String json;

    public SocketMessage(String command, String json) {
        this.command = command;
        this.json = json;
    }

    public String getCommand() {
        return command;
    }

    public String getJson() {
        return json;
    }
}
