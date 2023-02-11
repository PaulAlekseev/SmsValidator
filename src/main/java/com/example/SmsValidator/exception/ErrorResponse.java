package com.example.SmsValidator.exception;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BaseResponse {

    private final String message;

    public ErrorResponse(Exception e) {
        this.ok = false;
        this.message = e.getLocalizedMessage();
    }

    public ErrorResponse() {
        this.ok = false;
        this.message = "Unknown Error";
    }
}