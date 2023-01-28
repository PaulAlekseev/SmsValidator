package com.example.SmsValidator.bean.authentication;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest extends BaseResponse {

  private String email;
  private String password;
}
