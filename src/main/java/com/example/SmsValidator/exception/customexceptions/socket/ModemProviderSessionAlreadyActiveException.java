package com.example.SmsValidator.exception.customexceptions.socket;

public class ModemProviderSessionAlreadyActiveException extends Exception{
    public ModemProviderSessionAlreadyActiveException(String message) {
        super(message);
    }
}
