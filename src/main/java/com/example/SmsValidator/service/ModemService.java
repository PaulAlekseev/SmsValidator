package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.reservemodem.GetOwnReservedModemsResponse;
import com.example.SmsValidator.bean.reservemodem.ReserveModemBaseResponse;
import com.example.SmsValidator.bean.reservemodem.ReserveModemErrorResponse;
import com.example.SmsValidator.bean.reservemodem.ReserveModemSuccessResponse;
import com.example.SmsValidator.entity.ModemEntity;
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

    public ReserveModemBaseResponse reserveModem(String UserEmail, int daysToReserve) {
        ModemEntity modem = modemEntityRepository.
                findFirstByReservedUntilLessThanEqualAndUsedServiceTypeEntityListEmptyOrderByIdDesc(new Date());
        if (modem == null) return new ReserveModemErrorResponse("Couldn't find modem");
        Date newDate = Date.from((LocalDate.now().plusDays(daysToReserve)).atStartOfDay(ZoneId.systemDefault()).toInstant());
        modem.setReservedUntil(newDate);
        modem.setReservedBy(userRepository.findFirstByEmail(UserEmail));
        return new ReserveModemSuccessResponse(Modem.toModel(modemEntityRepository.save(modem)));
    }

    public ReserveModemBaseResponse reserveModem(Principal principal, int daysToReserve) {
        return reserveModem(principal.getName(), daysToReserve);
    }

    public GetOwnReservedModemsResponse getReservedModems(Long userId) {
        return new GetOwnReservedModemsResponse(modemEntityRepository.
                findByReservedBy_IdAndReservedUntilLessThanOrderByReservedUntilDesc(userId, new Date())
                .stream()
                .map(Modem::toModel)
                .collect(Collectors.toList()));
    }

    public GetOwnReservedModemsResponse getReservedModems(Principal principal) {
        return getReservedModems(userRepository.findByEmail(principal.getName()).get().getId());
    }
}
