package com.example.SmsValidator.bean.servicetype;

import com.example.SmsValidator.bean.BaseResponse;
import com.example.SmsValidator.entity.ServiceTypeEntity;
import com.example.SmsValidator.model.ServiceType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
public class GetAllowedServiceTypesResponse extends BaseResponse {
    private final List<ServiceType> services;
    private final int amount;

    public GetAllowedServiceTypesResponse(List<ServiceType> services) {
        this.services = services;
        this.amount = services.size();
    }
}
