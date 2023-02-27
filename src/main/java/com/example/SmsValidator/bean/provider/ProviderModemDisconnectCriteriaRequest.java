package com.example.SmsValidator.bean.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProviderModemDisconnectCriteriaRequest {
    private boolean byRevenue = false;
    private int revenue = 0;
    private boolean byService = false;
    private String services = "";
}
