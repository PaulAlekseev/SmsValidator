package com.example.SmsValidator.bean.payment.coinremitter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "payment.coin-remitter")
@Getter
@Setter
public class CoinRemitterPaymentInfo {
    private Map<String, CoinRemitterCoinInfo> coins;
    private String currency;
    private int expireTime;
    private String notifyUrl;
    private String invoiceUrl;
    private String qrUrl;
}
