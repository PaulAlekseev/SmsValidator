package com.example.SmsValidator.bean.provider;

import com.example.SmsValidator.dto.ModemDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ProviderModemsSuccessResponse extends ProviderModemsBaseResponse{
    private List<ModemDto> data;
}
