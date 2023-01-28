package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.servicetype.GetAmountOfAllowedModemBaseResponse;
import com.example.SmsValidator.bean.servicetype.GetAllowedServiceTypesResponse;
import com.example.SmsValidator.bean.servicetype.GetAmountOfAllowedModemErrorResponse;
import com.example.SmsValidator.bean.servicetype.GetAmountOfAllowedModemSuccessResponse;
import com.example.SmsValidator.entity.ServiceTypeEntity;
import com.example.SmsValidator.model.ServiceType;
import com.example.SmsValidator.repository.ServiceTypeEntityRepository;
import com.example.SmsValidator.repository.UsedServiceTypeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private static final String SERVICE_ID_ERROR = "Service with this id does not exist";
    private final ServiceTypeEntityRepository serviceTypeRepository;
    private final UsedServiceTypeEntityRepository usedServiceTypeEntityRepository;

    public GetAllowedServiceTypesResponse getActiveServices() {
        List<ServiceTypeEntity> activeServices = serviceTypeRepository.findByActiveTrue();
        return new GetAllowedServiceTypesResponse(activeServices
                .stream()
                .map(ServiceType::toModel)
                .collect(Collectors.toList()));
    }

    public int getAmountOfAllowedModems(ServiceTypeEntity serviceTypeEntity) {
        return (int) usedServiceTypeEntityRepository.
                countByModemEntity_BusyFalseAndTimesUsedLessThanAndLastTimeUsedLessThanEqual(
                        serviceTypeEntity.getAllowedAmount(),
                        Date.from((LocalDate.now()
                                .minusDays(serviceTypeEntity.getDaysBetween()))
                                .atStartOfDay(ZoneId.systemDefault()).toInstant())
                );
    }

    public GetAmountOfAllowedModemBaseResponse getAllowedModems(ServiceTypeEntity serviceType) {
        if(serviceType == null) return new GetAmountOfAllowedModemErrorResponse(SERVICE_ID_ERROR);
        return new GetAmountOfAllowedModemSuccessResponse(ServiceType.toModel(serviceType), getAmountOfAllowedModems(serviceType));
    }

    public GetAmountOfAllowedModemBaseResponse getAllowedModems(Long serviceTypeId) {
        ServiceTypeEntity serviceType = serviceTypeRepository.findFirstById(serviceTypeId);
        return getAllowedModems(serviceType);
    }
}
