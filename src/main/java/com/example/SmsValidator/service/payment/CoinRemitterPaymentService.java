package com.example.SmsValidator.service.payment;

import com.example.SmsValidator.bean.payment.coinremitter.CoinRemitterPaymentInfo;
import com.example.SmsValidator.bean.payment.coinremitter.CoinRemitterSiteResponse;
import com.example.SmsValidator.bean.payment.coinremitter.CoinRemitterSiteResponseProvider;
import com.example.SmsValidator.bean.payment.coinremitter.CoinRemitterSuccessResponse;
import com.example.SmsValidator.controller.payment.coinremitter.CoinRemitterPaymentProvider;
import com.example.SmsValidator.exception.customexceptions.payment.UnknownCoinException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CoinRemitterPaymentService {

    private final CoinRemitterPaymentProvider paymentProvider;

    public String sendInvoice(String coin, int amount) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate
                .postForObject(
                        paymentProvider.createInvoiceUrl(coin),
                        paymentProvider.createInvoiceRequest(coin, amount),
                        String.class);
        CoinRemitterSiteResponseProvider responseProvider = new CoinRemitterSiteResponseProvider(response);
        return response;
    }

    public CoinRemitterSuccessResponse createResponse(String jsonData) throws UnknownCoinException {
        CoinRemitterSuccessResponse response = new CoinRemitterSuccessResponse();
        CoinRemitterSiteResponseProvider responseProvider = new CoinRemitterSiteResponseProvider(jsonData);
        response.setCoin(responseProvider.getCoin());
        response.setUrl(responseProvider.getUrl());
        response.setAddress(responseProvider.getAddress());
        response.setAmountInCoin(responseProvider.getAmount());
        response.setQrCode(responseProvider.createQrCode(paymentProvider));
        response.setAmountInUSD(responseProvider.getAmountUsd());
        return response;
    }

    public CoinRemitterSuccessResponse createInvoice(String coin, int amount) throws Exception {
        String remitterResponse = sendInvoice(coin, amount);
        return createResponse(remitterResponse);
    }
}
