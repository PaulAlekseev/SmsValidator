package com.example.SmsValidator.exception;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BaseResponse {
    private final boolean ok = false;

    private final String message;

    public ErrorResponse(Exception e) {
        this.message = e.getLocalizedMessage();
    }

    public ErrorResponse() {
        this.message = "Unknown Error";
    }

    public ErrorResponse(String errorMessage) {
        this.message = errorMessage;
    }
}