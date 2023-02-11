package com.example.SmsValidator.controller.payment.coinremitter;

import com.example.SmsValidator.bean.payment.coinremitter.CoinRemitterCoinInfo;
import com.example.SmsValidator.bean.payment.coinremitter.CoinRemitterPaymentInfo;
import com.example.SmsValidator.exception.customexceptions.payment.UnknownCoinException;
import com.example.SmsValidator.exception.customexceptions.socket.CouldNotParseSocketMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class CoinRemitterPaymentProvider {

    private final CoinRemitterPaymentInfo coinRemitterPaymentInfo;

    public HttpEntity<MultiValueMap<String, String>> createInvoiceRequest(String coin, int amount) throws UnknownCoinException {
        if (!checkIfExists(coin)) {
            throw new UnknownCoinException("Unknown coin");
        }
        CoinRemitterCoinInfo coinInfo = coinRemitterPaymentInfo.getCoins().get(coin);
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("amount", String.valueOf(amount));
        map.add("currency", coinRemitterPaymentInfo.getCurrency());
        map.add("api_key", coinInfo.getApiKey());
        map.add("password", coinInfo.getPassword());
        map.add("expire_time", String.valueOf(coinRemitterPaymentInfo.getExpireTime()));
        map.add("notify_url", coinRemitterPaymentInfo.getNotifyUrl());

        return new HttpEntity<>(map, headers);
    }

    public boolean checkIfExists(String coinName) {
        return coinRemitterPaymentInfo.getCoins().containsKey(coinName);
    }

    public String createInvoiceUrl(String coinName) {
        return String.format(coinRemitterPaymentInfo.getInvoiceUrl(), coinName);
    }

    public CoinRemitterCoinInfo getCoinInfo(String coin) throws UnknownCoinException {
        if (!checkIfExists(coin)) throw new UnknownCoinException("Unknown coin");
        return coinRemitterPaymentInfo.getCoins().get(coin);
    }

    public CoinRemitterPaymentInfo getInfo() {
        return coinRemitterPaymentInfo;
    }
}
