package com.phonenumber.scheduler;

import com.phonenumber.constant.PhoneNumberStatusConstants;
import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.repository.PhoneNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class PhoneNumberUnlockScheduler {

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Value("${scheduler.locked.duration}")
    private long lockedDuration;

    @Scheduled(fixedRateString = "${locked.scheduler.job.frequency}")
    public void unlockPhoneNumbers() {
        Date cutoffTime = new Date(System.currentTimeMillis() - lockedDuration);
        List<PhoneNumberModel> lockedPhoneNumbers = phoneNumberRepository.findByStatusAndUpdateDateBefore(PhoneNumberStatusConstants.LOCKED.getStatus(), cutoffTime);

        lockedPhoneNumbers.forEach(phoneNumber -> {
            phoneNumber.setStatus(PhoneNumberStatusConstants.AVAILABLE.getStatus());
            phoneNumber.setUpdateDate(new Date());
            phoneNumberRepository.save(phoneNumber);
        });

        log.info("Unlocked phone numbers:" + lockedPhoneNumbers.size());

    }


}