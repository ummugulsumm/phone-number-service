package com.phonenumber.service;

public interface SmsService {
    void sendSms(String phoneNumber, String contactPhoneNumber, String message);
}
