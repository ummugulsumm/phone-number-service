package com.phonenumber.service;

import com.phonenumber.dto.PhoneNumberDto;
import com.phonenumber.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhoneNumberService {
    ResponseModel getAvailablePhoneNumbers();

    @Transactional
    ResponseModel getAvailablePhoneNumbersByCount(Integer count);

    PhoneNumberDto createPhoneNumber(PhoneNumberDto newPhoneNumber);

    void updateStatusAvailable();

    ResponseModel getAvailablePhoneNumbersByDigit(String digit);

    void updateStatusSold(String phoneNumberId);

    ResponseEntity<Void> addContactPhoneNumber(String phoneNumberId, String contactPhoneNumber);

    String getAiHelp(List<Object> phoneNumbers);

    PhoneNumberDto updateStatusSoldActive(String phoneNumberId);
}
