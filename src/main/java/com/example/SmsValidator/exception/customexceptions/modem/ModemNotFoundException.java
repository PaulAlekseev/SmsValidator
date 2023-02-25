package com.example.SmsValidator.exception.customexceptions.modem;

import com.example.SmsValidator.exception.CustomException;

public class ModemNotFoundException extends CustomException {
    public ModemNotFoundException(String message) {
        super(message);
    }
}
