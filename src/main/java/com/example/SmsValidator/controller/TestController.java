package com.example.SmsValidator.controller;

import com.example.SmsValidator.dto.ModemDto;
import com.example.SmsValidator.entity.InvoiceEntity;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemEntity_;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.repository.InvoiceEntityRepository;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.ModemProviderSessionEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import com.example.SmsValidator.service.PaymentService;
import com.example.SmsValidator.service.ProviderService;
import com.example.SmsValidator.specification.ModemSpecification;
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
    private final ModemProviderSessionEntityRepository modemProviderSessionEntityRepository;
    private final ModemEntityRepository modemEntityRepository;

    @PostMapping("createInvoice")
    public ResponseEntity<?> createInvoice(@RequestParam int amount) throws Exception {
        return ResponseEntity.ok(paymentService.createInvoiceEntity(
                userRepository.findFirstByEmail(SecurityContextHolder.getContext().getAuthentication().getName()),
                amount
        ));
    }

    @PostMapping("test")
    public ResponseEntity<?> test(@RequestParam int id) {
        ModelMapper modelMapper = new ModelMapper();
        List<ModemDto> modem = modemEntityRepository
                .findAll(
                        ModemSpecification.hasModemProviderSessionId((long) id).and(
                                ModemSpecification.withTasks())
                )
                .stream()
                .map((entity) -> modelMapper.map(entity, ModemDto.class))
                .toList();
        return ResponseEntity.ok(modem);
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
