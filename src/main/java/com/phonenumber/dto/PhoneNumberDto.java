package com.phonenumber.dto;

import lombok.Data;

@Data
public class PhoneNumberDto {

    private String phoneNumberId;

    private String phoneNumber;

    private String specialPhoneNumberType;

    private int specialPhoneNumberPrice;
}
