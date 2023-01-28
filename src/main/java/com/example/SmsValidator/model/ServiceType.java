package com.example.SmsValidator.model;

import com.example.SmsValidator.entity.ServiceTypeEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceType {
    private String name;
    private int allowedAmount;
    private int daysBetween;

    private ServiceType() {
    }

    public static ServiceType toModel(ServiceTypeEntity serviceTypeEntity) {
        ServiceType serviceType = new ServiceType();
        serviceType.setName(serviceTypeEntity.getName());
        serviceType.setDaysBetween(serviceTypeEntity.getDaysBetween());
        serviceType.setAllowedAmount(serviceTypeEntity.getAllowedAmount());
        return serviceType;
    }
}
