package com.example.SmsValidator.bean.reservemodem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveModemRequest {
    private int reserveFor;
}
