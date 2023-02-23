package com.example.SmsValidator.controller;

import com.example.SmsValidator.entity.InvoiceEntity;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.entity.projection.UserDto;
import com.example.SmsValidator.repository.InvoiceEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import com.example.SmsValidator.service.PaymentService;
import com.example.SmsValidator.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/test/")
@RequiredArgsConstructor
public class TestController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final InvoiceEntityRepository invoiceEntityRepository;
    private final ProviderService providerService;

    @PostMapping("createInvoice")
    public ResponseEntity<?> createInvoice(@RequestParam int amount) throws Exception {
        return ResponseEntity.ok(paymentService.createInvoiceEntity(
                userRepository.findFirstByEmail(SecurityContextHolder.getContext().getAuthentication().getName()),
                amount
        ));
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
