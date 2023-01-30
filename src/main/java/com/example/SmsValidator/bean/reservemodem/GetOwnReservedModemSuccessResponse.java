package com.example.SmsValidator.bean.reservemodem;

import com.example.SmsValidator.model.Modem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOwnReservedModemSuccessResponse extends GetOwnReservedModemBaseResponse {
    private List<Modem> reservedModems;
}
