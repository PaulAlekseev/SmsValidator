package com.example.SmsValidator.bean.payment.coinremitter;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinRemitterSuccessResponse extends BaseResponse {
    private String url;
    private String address;
    private String qrCode;
    private String coin;
    private String amountInCoin;
    private String amountInUSD;
}
