package com.phonenumber.scheduler;

import com.phonenumber.constant.PhoneNumberStatusConstants;
import com.phonenumber.constant.SmsMessageConstants;
import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.repository.PhoneNumberRepository;
import com.phonenumber.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class PhoneNumberReleaseScheduler {



    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private SmsService smsService;
    @Value("${scheduler.hold.duration}")
    private long holdDuration;

    @Value("${scheduler.sold.duration}")
    private long soldDuration;

    @Scheduled(fixedRateString = "${hold.scheduler.job.frequency}")
    public void releasePhoneNumbersOfHold() {
        Date cutoffTime = new Date(System.currentTimeMillis() - holdDuration);
        List<PhoneNumberModel> holdPhoneNumbers = phoneNumberRepository.findByStatusAndUpdateDateBefore(PhoneNumberStatusConstants.HOLD.getStatus(), cutoffTime);

        holdPhoneNumbers.forEach(phoneNumber -> {
            smsService.sendSms(phoneNumber.getPhoneNumber(), phoneNumber.getContactPhoneNumber(), SmsMessageConstants.RELEASE_HOLD.getMessage(phoneNumber.getPhoneNumber()));
            phoneNumber.setStatus(PhoneNumberStatusConstants.AVAILABLE.getStatus());
            phoneNumber.setContactPhoneNumber("");
            phoneNumber.setUpdateDate(new Date());
            phoneNumberRepository.save(phoneNumber);

        });


        log.info("Released phone numbers:(hold)" + holdPhoneNumbers.size());

    }

    @Scheduled(fixedRateString = "${sold.scheduler.job.frequency}")
    public void releasePhoneNumbersOfSold() {
        Date cutoffTime = new Date(System.currentTimeMillis() - soldDuration);
        List<PhoneNumberModel> soldPhoneNumbers = phoneNumberRepository.findByStatusAndUpdateDateBefore(PhoneNumberStatusConstants.SOLD.getStatus(), cutoffTime);

        soldPhoneNumbers.forEach(phoneNumber -> {
            smsService.sendSms(phoneNumber.getPhoneNumber(), phoneNumber.getContactPhoneNumber(), SmsMessageConstants.RELEASE_SOLD.getMessage(phoneNumber.getPhoneNumber()));
            phoneNumber.setStatus(PhoneNumberStatusConstants.AVAILABLE.getStatus());
            phoneNumber.setContactPhoneNumber("");
            phoneNumber.setUpdateDate(new Date());
            phoneNumberRepository.save(phoneNumber);

        });


        log.info("Released phone numbers(sold):" + soldPhoneNumbers.size());

    }

}
