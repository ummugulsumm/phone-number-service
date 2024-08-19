package com.phonenumber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
public class PhoneNumberApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneNumberApplication.class, args);
	}

}
