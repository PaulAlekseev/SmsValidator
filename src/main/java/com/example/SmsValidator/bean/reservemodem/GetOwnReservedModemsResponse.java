package com.example.SmsValidator.bean.reservemodem;

import com.example.SmsValidator.bean.BaseResponse;
import com.example.SmsValidator.model.Modem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOwnReservedModemsResponse extends BaseResponse {
    private List<Modem> reservedModems;
}
