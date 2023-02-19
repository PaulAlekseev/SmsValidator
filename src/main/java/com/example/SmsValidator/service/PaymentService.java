package com.example.SmsValidator.service;

import com.example.SmsValidator.entity.InvoiceEntity;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.exception.customexceptions.payment.NotValidInvoiceException;
import com.example.SmsValidator.repository.InvoiceEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final InvoiceEntityRepository invoiceEntityRepository;

    public InvoiceEntity createInvoiceEntity(User user, int amount) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setAmount(amount);
        invoiceEntity.setUser(user);
        return invoiceEntityRepository.save(invoiceEntity);
    }

    public void checkInvoice(Long invoiceId, User user, int amount) throws NotValidInvoiceException, Error {
        if (invoiceEntityRepository
                .updateValidatedByValidatedFalseAndIdAndAmountAndUser(true, invoiceId, amount, user) == 0)
            throw new NotValidInvoiceException("Invoice is not valid");
    }

    public boolean topUp(Long userId, int amount) {
        return userRepository.topUpBalance(userId, amount) > 0;
    }

    public String topUpBalance(int amount, int userId, Long invoiceId) throws NotValidInvoiceException, Error {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        checkInvoice(invoiceId, user, amount);
        return topUp(user.getId(), amount) ?
                "OK" :
                "NOT OK";
    }
}
