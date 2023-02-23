package com.example.SmsValidator.exception.customexceptions.provider;

import com.example.SmsValidator.exception.CustomException;

public class ProviderSessionNotFoundException extends CustomException {
    public ProviderSessionNotFoundException(String message) {
        super(message);
    }
}
