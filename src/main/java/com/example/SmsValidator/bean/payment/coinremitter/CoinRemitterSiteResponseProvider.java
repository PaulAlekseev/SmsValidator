package com.example.SmsValidator.bean.payment.coinremitter;

import com.example.SmsValidator.controller.payment.coinremitter.CoinRemitterPaymentProvider;
import com.example.SmsValidator.exception.customexceptions.payment.UnknownCoinException;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

public class CoinRemitterSiteResponseProvider {
    @Getter
    private final String url;
    @Getter
    private final String address;
    @Getter
    private final String coin;
    @Getter
    private final String amount;
    @Getter
    private final String amountUsd;

    public CoinRemitterSiteResponseProvider(String jsonData) {
        Gson gson = new Gson();
        CoinRemitterSiteResponseData data = new Gson()
                .fromJson(jsonData, CoinRemitterSiteResponse.class)
                .getData();
        this.url = data.getUrl();
        this.address = data.getAddress();
        this.coin = data.getCoin();
        this.amountUsd = data.getUsd_amount();
        this.amount = JsonParser.parseString(jsonData).getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("total_amount").getAsJsonObject()
                .get(data.getCoin()).getAsString();
    }

    public String createQrCode(CoinRemitterPaymentProvider provider) throws UnknownCoinException {
        return UriComponentsBuilder
                .fromUriString(provider.getInfo().getQrUrl())
                .queryParam("chs", "300x300")
                .queryParam("chld", "L|2")
                .queryParam("cht", "qr")
                .queryParam("chl",
                        provider.getCoinInfo(this.coin).getQrName() + ":" + this.address + "?amount=" + this.amount)
                .build().toString();
    }
}
