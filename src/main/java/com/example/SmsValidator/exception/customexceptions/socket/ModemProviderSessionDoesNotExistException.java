package com.example.SmsValidator.exception.customexceptions.socket;

import com.example.SmsValidator.exception.CustomException;

public class ModemProviderSessionDoesNotExistException extends CustomException {
    public ModemProviderSessionDoesNotExistException(String message) {
        super(message);
    }
}
