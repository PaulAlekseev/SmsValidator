package com.example.SmsValidator.bean.reservemodem;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetOwnReservedModemErrorResponse extends GetOwnReservedModemBaseResponse {
    private String errorMessage;

    public GetOwnReservedModemErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.ok = false;
    }
}
