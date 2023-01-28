package com.example.SmsValidator.bean.refreshtoken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenFailResponse extends RefreshTokenBaseResponse {
    private String errorMessage;

    public RefreshTokenFailResponse(String errorMessage) {
        this.ok = false;
        this.errorMessage = errorMessage;
    }
}
