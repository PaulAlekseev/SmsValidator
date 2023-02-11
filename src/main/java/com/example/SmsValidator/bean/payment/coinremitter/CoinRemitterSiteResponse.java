package com.example.SmsValidator.bean.payment.coinremitter;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinRemitterSiteResponse extends BaseResponse {
    private int flag;
    private String msg;

    private CoinRemitterSiteResponseData data;

}
