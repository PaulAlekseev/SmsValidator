package com.example.SmsValidator.service;

import com.example.SmsValidator.bean.servicetype.*;
import com.example.SmsValidator.entity.ModemEntity;
import com.example.SmsValidator.entity.ServiceTypeEntity;
import com.example.SmsValidator.entity.UsedServiceTypeEntity;
import com.example.SmsValidator.model.ServiceType;
import com.example.SmsValidator.repository.ModemEntityRepository;
import com.example.SmsValidator.repository.ServiceTypeEntityRepository;
import com.example.SmsValidator.repository.UsedServiceTypeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {
    private final ServiceTypeEntityRepository serviceTypeRepository;
    private final UsedServiceTypeEntityRepository usedServiceTypeEntityRepository;
    private final TaskService taskService;
    private final ModemEntityRepository modemEntityRepository;

    public GetAllowedServiceTypesResponse getActiveServices() {
        List<ServiceTypeEntity> activeServices = serviceTypeRepository.findByActiveTrue();
        return new GetAllowedServiceTypesResponse(activeServices
                .stream()
                .map(ServiceType::toModel)
                .collect(Collectors.toList()));
    }

    public ServiceTypeEntity createNewServiceEntity(CreateNewServiceRequest request) {
        ServiceTypeEntity newService = new ServiceTypeEntity();
        newService.setActive(true);
        newService.setName(request.getName());
        newService.setAllowedAmount(request.getAllowedAmount());
        newService.setDaysBetween(request.getDaysBetween());
        newService.setMessageRegex(request.getMessageRegex());
        newService.setSenderRegex(request.getSenderRegex());
        newService.setTimeSeconds(request.getTimeSeconds());
        return serviceTypeRepository.save(newService);
    }

    public List<UsedServiceTypeEntity> createUsedServices(ServiceTypeEntity serviceType) {
        List<ModemEntity> modems = (List<ModemEntity>) modemEntityRepository.findAll();
        List<UsedServiceTypeEntity> newUsedServices = new ArrayList<>();
        for (ModemEntity modem : modems) {
            UsedServiceTypeEntity usedServiceTypeEntity = new UsedServiceTypeEntity();
            usedServiceTypeEntity.setModemEntity(modem);
            usedServiceTypeEntity.setServiceType(serviceType);
            newUsedServices.add(usedServiceTypeEntity);
        }
        return (List<UsedServiceTypeEntity>) usedServiceTypeEntityRepository.saveAll(newUsedServices);
    }

    public CreateNewServiceBaseResponse createNewService(CreateNewServiceRequest request) {
        ServiceTypeEntity newService = createNewServiceEntity(request);
        createUsedServices(newService);
        CreateNewServiceSuccessResponse response = new CreateNewServiceSuccessResponse();
        response.setServiceType(newService);
        return response;
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

    public GetAmountOfAllowedModemBaseResponse getAmountOfAvailable(String serviceAbbreviation) {
        int amount = taskService.getAmountOfAvailableServices(serviceAbbreviation);
        return GetAmountOfAllowedModemSuccessResponse.builder()
                .amount(amount)
                .service(serviceAbbreviation)
                .build();
    }
}
