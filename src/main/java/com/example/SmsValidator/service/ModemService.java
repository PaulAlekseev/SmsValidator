package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.reservemodem.*;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModemService {

    private final ModemEntityRepository modemEntityRepository;
    private final UserRepository userRepository;

    public ModemEntity getAvailableForReserveModem() {
        return modemEntityRepository
                .findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityList_TimesUsedOrderByIdDesc(0);
//                .findFirstByBusyFalseAndReservedUntilLessThanAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityListEmptyOrderByIdDesc(new Date());
    }

    public ReserveModemBaseResponse reserveModem(Principal principal, int daysToReserve) {
        return reserveModem(principal.getName(), daysToReserve);
    }

    public ReserveModemBaseResponse reserveModem(String userEmail, int daysToReserve) {
        User user = userRepository.findFirstByEmail(userEmail);
        return reserveModem(user, daysToReserve);
    }

    public ReserveModemBaseResponse reserveModem(Long telegramId, int daysToReserve) {
        User user = userRepository.findFirstByTelegramId(telegramId);
        return reserveModem(user, daysToReserve);
    }

    public ReserveModemBaseResponse reserveModem(User user, int daysToReserve) {
        if (user == null) return new ReserveModemErrorResponse("Couldn't find user");
        ModemEntity modem = getAvailableForReserveModem();
        if (modem == null) return new ReserveModemErrorResponse("Couldn't find modem");
        Date newDate = Date.from((LocalDate.now().plusDays(daysToReserve)).atStartOfDay(ZoneId.systemDefault()).toInstant());
        modem.setReservedUntil(newDate);
        modem.setReservedBy(user);
        return new ReserveModemSuccessResponse(Modem.toModel(modemEntityRepository.save(modem)));
    }

    public GetOwnReservedModemBaseResponse getReservedModems(User user) {
        if (user == null) {
            return new GetOwnReservedModemErrorResponse("Could not find user");
        }
        return new GetOwnReservedModemSuccessResponse(modemEntityRepository
                .findByReservedBy_IdAndReservedUntilGreaterThanEqualOrderByReservedUntilDesc(user.getId(), new Date())
                .stream()
                .map(Modem::toModel)
                .collect(Collectors.toList()));
    }

    public GetOwnReservedModemBaseResponse getReservedModems(Long telegramId) {
        return getReservedModems(userRepository.findFirstByTelegramId(telegramId));
    }

    public GetOwnReservedModemBaseResponse getReservedModems(Principal principal) {
        return getReservedModems(userRepository.findFirstByEmail(principal.getName()));
    }
}
