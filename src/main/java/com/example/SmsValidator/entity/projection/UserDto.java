package com.example.SmsValidator.entity.projection;

import com.example.SmsValidator.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;

    private String password;
    private List<InvoiceDto> invoices;
}
