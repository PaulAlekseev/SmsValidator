package com.example.SmsValidator.exception.customexceptions.provider;

import com.example.SmsValidator.exception.CustomException;

public class ProviderSessionBusyException extends CustomException {
    public ProviderSessionBusyException(String message) {
        super(message);
    }
}
