package com.example.SmsValidator.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenSuccessResponse extends RefreshTokenBaseResponse{
    private String authToken;

    public RefreshTokenSuccessResponse(String authToken) {
        this.authToken = authToken;
    }
}
