package com.phonenumber.scheduler;

import com.phonenumber.constant.PhoneNumberStatusConstants;
import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PhoneNumberUnlockScheduler {

    private static final Logger logger = Logger.getLogger(PhoneNumberUnlockScheduler.class.getName());


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

        String message = "Unlocked phone numbers:" + lockedPhoneNumbers.size();
        logger.info(message);

    }


}