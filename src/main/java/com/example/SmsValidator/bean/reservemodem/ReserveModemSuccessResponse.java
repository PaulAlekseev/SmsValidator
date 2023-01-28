package com.example.SmsValidator.bean.reservemodem;

import com.example.SmsValidator.model.Modem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReserveModemSuccessResponse extends ReserveModemBaseResponse {
    private Modem modem;

    public ReserveModemSuccessResponse(Modem modem) {
        this.modem = modem;
    }
}
