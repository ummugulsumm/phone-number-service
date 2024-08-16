package com.phonenumber.service.impl;

import com.phonenumber.model.SmsModel;
import com.phonenumber.repository.SmsRepository;
import com.phonenumber.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final SmsRepository smsRepository;

    public SmsServiceImpl(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }


    @Override
    public void sendSms(String phoneNumber, String contactPhoneNumber, String message) {
        SmsModel sms = new SmsModel();
        sms.setContactPhoneNumber(contactPhoneNumber);
        sms.setSpecialPhoneNumber(phoneNumber);
        sms.setMessage(message);
        sms.setSendAt(LocalDateTime.now());
        smsRepository.save(sms);
        log.info("Sending SMS to " + contactPhoneNumber + ": " + message);
    }

}
