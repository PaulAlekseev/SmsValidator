package com.example.SmsValidator.socket.handler;

import com.example.SmsValidator.exception.CouldNotParseSocketMessageException;
import com.example.SmsValidator.socket.SocketMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketMessageParser {
    private static final String regex = "^(\\w+):([\\{|\\[].*[\\]|\\}])";

    public static SocketMessage parseMessage(String message) throws CouldNotParseSocketMessageException {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return new SocketMessage(matcher.group(1), matcher.group(2));
        }
        throw new CouldNotParseSocketMessageException("Could not parse socket message");
    }
}
