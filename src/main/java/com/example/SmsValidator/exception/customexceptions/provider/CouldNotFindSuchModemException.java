package com.example.SmsValidator.exception.customexceptions.provider;

import com.example.SmsValidator.exception.CustomException;

public class CouldNotFindSuchModemException extends CustomException {
    public CouldNotFindSuchModemException(String message) {
        super(message);
    }
}
