package com.example.SmsValidator.bean.payment.coinremitter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinRemitterSiteResponseData {
    private String url;
    private String address;
    private String coin;
    private String usd_amount;
}
