package com.example.SmsValidator.bean.servicetype;

import com.example.SmsValidator.entity.ServiceTypeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewServiceSuccessResponse extends CreateNewServiceBaseResponse{
    private ServiceTypeEntity serviceType;
}
