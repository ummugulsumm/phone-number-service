package com.phonenumber.service;

import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface PhoneNumberService {
    ResponseModel getAvailablePhoneNumbers();

    @Transactional
    ResponseModel getAvailablePhoneNumbersByCount(Integer count);

    PhoneNumberModel createPhoneNumber(PhoneNumberModel newPhoneNumber);

    void deletePhoneNumber(String id);

    void updateStatusAvailable();

    ResponseModel getAvailablePhoneNumbersByDigit(String digit);

    void updateStatusSold(String phoneNumberId);

    ResponseEntity<Void> addContactPhoneNumber(String phoneNumberId, String contactPhoneNumber);
}
