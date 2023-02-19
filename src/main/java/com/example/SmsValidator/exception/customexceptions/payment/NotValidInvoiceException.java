package com.example.SmsValidator.exception.customexceptions.payment;

import com.example.SmsValidator.exception.CustomException;

public class NotValidInvoiceException extends CustomException {
    public NotValidInvoiceException(String message) {
        super(message);
    }
}
