package com.example.SmsValidator.controller.payment.coinremitter;

import com.example.SmsValidator.service.payment.CoinRemitterPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/payment/coinRemitter")
@RequiredArgsConstructor
public class CoinRemitterPaymentController {

    private final CoinRemitterPaymentService coinRemitterPaymentService;

    @PostMapping(path = "createInvoice")
    public ResponseEntity<?> pay(@RequestParam String coin, @RequestParam int amount) throws Exception {
        return ResponseEntity.ok(coinRemitterPaymentService.createInvoice(coin, amount));
    }

    @PostMapping(path = "test")
    public ResponseEntity<?> test(@RequestParam String cum) {
        return ResponseEntity.ok(cum);
    }

}
