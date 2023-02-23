package com.example.SmsValidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SmsValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsValidatorApplication.class, args);
	}

}
