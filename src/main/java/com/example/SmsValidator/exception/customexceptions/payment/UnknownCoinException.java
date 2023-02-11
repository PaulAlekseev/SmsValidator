package com.example.SmsValidator.exception.customexceptions.payment;

import com.example.SmsValidator.exception.CustomException;

public class UnknownCoinException extends CustomException {
    public UnknownCoinException(String message) {
        super(message);
    }
}
