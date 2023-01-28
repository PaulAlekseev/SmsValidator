package com.example.SmsValidator.bean.reservemodem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveModemErrorResponse extends ReserveModemBaseResponse {
    private String message;

    public ReserveModemErrorResponse(String message) {
        this.ok = false;
        this.message = message;
    }
}
