package com.example.SmsValidator.bean.authentication;

import com.example.SmsValidator.bean.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.cfg.BaselineSessionEventsListenerBuilder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends BaseResponse {

  private String username;
  private String email;
  private String password;
}
