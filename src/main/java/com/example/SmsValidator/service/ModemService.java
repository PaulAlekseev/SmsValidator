package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.reservemodem.*;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ModemEntity_;
import com.example.SmsValidator.entity.ModemProviderSessionEntity;
import com.example.SmsValidator.entity.User;
import com.example.SmsValidator.exception.customexceptions.modem.ModemNotFoundException;
import com.example.SmsValidator.model.Modem;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.UserRepository;
import com.example.SmsValidator.specification.ModemSpecification;
import com.example.SmsValidator.specification.extra.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public ModemEntity getAvailableForReserveModem() throws ModemNotFoundException {
        Specification<ModemEntity> modemSpecification = ModemSpecification
                .withTimesUsedLessThanOrEqual(0)
                .and(ModemSpecification.withTasks())
                .and(ModemSpecification.hasBusy(false))
                .and(ModemSpecification.hasModemProviderSessionEntity_Active(true))
                .and(ModemSpecification.hasModemProviderSessionEntity_Busy(false))
                .and(ModemSpecification.orderBy(Order.DESC, ModemEntity_.ID));
        return modemEntityRepository
                .findAll(modemSpecification)
                .stream().findFirst()
                .orElseThrow(() -> new ModemNotFoundException("Could not find such modem"));
//                .findFirstByBusyFalseAndModemProviderSessionEntity_BusyFalseAndModemProviderSessionEntity_ActiveTrueAndUsedServiceTypeEntityList_TimesUsedOrderByIdDesc(0);
//                .findFirstByBusyFalseAndReservedUntilLessThanAndModemProviderSessionEntity_ActiveTrueAndModemProviderSessionEntity_BusyFalseAndUsedServiceTypeEntityListEmptyOrderByIdDesc(new Date());
    }

    public ReserveModemBaseResponse reserveModem(Principal principal, int daysToReserve) throws ModemNotFoundException {
        return reserveModem(principal.getName(), daysToReserve);
    }

    public ReserveModemBaseResponse reserveModem(String userEmail, int daysToReserve) throws ModemNotFoundException {
        User user = userRepository.findFirstByEmail(userEmail);
        return reserveModem(user, daysToReserve);
    }

    public ReserveModemBaseResponse reserveModem(Long telegramId, int daysToReserve) throws ModemNotFoundException {
        User user = userRepository.findFirstByTelegramId(telegramId);
        return reserveModem(user, daysToReserve);
    }

    public ReserveModemBaseResponse reserveModem(User user, int daysToReserve) throws ModemNotFoundException {
        if (user == null) return new ReserveModemErrorResponse("Couldn't find user");
        ModemEntity modem = getAvailableForReserveModem();
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
