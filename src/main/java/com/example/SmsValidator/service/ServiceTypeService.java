package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.servicetype.GetAmountOfAllowedModemBaseResponse;
import com.example.SmsValidator.bean.servicetype.GetAllowedServiceTypesResponse;
import com.example.SmsValidator.bean.servicetype.GetAmountOfAllowedModemErrorResponse;
import com.example.SmsValidator.bean.servicetype.GetAmountOfAllowedModemSuccessResponse;
import com.example.SmsValidator.entity.ModemEntity;
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
    private static final String NO_AVAILABLE_MODEM = "There is no allowed modem";
    private final ServiceTypeEntityRepository serviceTypeRepository;
    private final UsedServiceTypeEntityRepository usedServiceTypeEntityRepository;
    private final TaskService taskService;

    public GetAllowedServiceTypesResponse getActiveServices() {
        List<ServiceTypeEntity> activeServices = serviceTypeRepository.findByActiveTrue();
        return new GetAllowedServiceTypesResponse(activeServices
                .stream()
                .map(ServiceType::toModel)
                .collect(Collectors.toList()));
    }

//    public ModemEntity getIfAllowedModemExists(ServiceTypeEntity serviceType) {
////        return (int) usedServiceTypeEntityRepository.
////                countByModemEntity_BusyFalseAndTimesUsedLessThanAndLastTimeUsedLessThanEqual(
////                        serviceTypeEntity.getAllowedAmount(),
////                        Date.from((LocalDate.now()
////                                .minusDays(serviceTypeEntity.getDaysBetween()))
////                                .atStartOfDay(ZoneId.systemDefault()).toInstant())
////                );
//        return taskService.getAvailableModem(serviceType);
//    }
//
//    public GetAmountOfAllowedModemBaseResponse getAvailableModem(ServiceTypeEntity serviceType) {
//        if(serviceType == null) return new GetAmountOfAllowedModemErrorResponse(SERVICE_ID_ERROR);
//        ModemEntity modem = getIfAllowedModemExists(serviceType);
//        if(modem == null) return new GetAmountOfAllowedModemSuccessResponse(ServiceType.toModel(serviceType), false);
//        return new GetAmountOfAllowedModemSuccessResponse(ServiceType.toModel(serviceType), true);
//    }
//
//    public GetAmountOfAllowedModemBaseResponse getAvailableModem(Long serviceTypeId) {
//        ServiceTypeEntity serviceType = serviceTypeRepository.findFirstById(serviceTypeId);
//        return getAvailableModem(serviceType);
//    }

    public GetAmountOfAllowedModemBaseResponse getAmountOfAvailable(Long serviceId) {
        try {
            ServiceTypeEntity serviceType = serviceTypeRepository.findById(serviceId).orElseThrow(() -> new Exception(SERVICE_ID_ERROR));
            return getAmountOfAvailable(serviceType);
        } catch (Exception e) {
            return new GetAmountOfAllowedModemErrorResponse(e.getLocalizedMessage());
        }
    }

    private GetAmountOfAllowedModemBaseResponse getAmountOfAvailable(ServiceTypeEntity serviceType) {
        int amount = taskService.getAmountOfAvailableServices(serviceType);
        return new GetAmountOfAllowedModemSuccessResponse(ServiceType.toModel(serviceType), amount);
    }
}
