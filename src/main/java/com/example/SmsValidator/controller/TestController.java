package com.example.SmsValidator.controller;

import com.example.SmsValidator.entity.InvoiceEntity;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.repository.InvoiceEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import com.example.SmsValidator.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "api/v1/test/")
@RequiredArgsConstructor
public class TestController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final InvoiceEntityRepository invoiceEntityRepository;

    @PostMapping("createInvoice")
    public ResponseEntity<?> createInvoice(@RequestParam int amount) throws Exception {
        return ResponseEntity.ok(paymentService.createInvoiceEntity(
                userRepository.findFirstByEmail(SecurityContextHolder.getContext().getAuthentication().getName()),
                amount
        ));
    }

    @GetMapping("sum")
    public ResponseEntity<?> something() {
        User user = userRepository.findFirstByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("things")
    public ResponseEntity<?> something1() {
        User user = userRepository.findFirstByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setUser(user);
        invoice.setCreated(new Date());
        invoice.setValidated(true);
        invoiceEntityRepository.save(invoice);
        return ResponseEntity.ok(user);
    }


    @PostMapping("topUp")
    public ResponseEntity<?> topUp(@RequestParam int amount, @RequestParam Long invoiceId, @RequestParam int userId)
            throws Exception {
        return ResponseEntity.ok(paymentService.topUpBalance(
                amount,
                userId,
                invoiceId
        ));
    }
}
