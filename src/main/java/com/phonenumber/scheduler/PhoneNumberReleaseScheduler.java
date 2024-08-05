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
public class PhoneNumberReleaseScheduler {

    private static final Logger logger = Logger.getLogger(PhoneNumberReleaseScheduler.class.getName());

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Value("${scheduler.hold.duration}")
    private long holdDuration;

    @Scheduled(fixedRateString = "${hold.scheduler.job.frequency}")
    public void unlockPhoneNumbers() {
        Date cutoffTime = new Date(System.currentTimeMillis() - holdDuration);
        List<PhoneNumberModel> holdPhoneNumbers = phoneNumberRepository.findByStatusAndUpdateDateBefore(PhoneNumberStatusConstants.HOLD.getStatus(), cutoffTime);

        holdPhoneNumbers.forEach(phoneNumber -> {
            phoneNumber.setStatus(PhoneNumberStatusConstants.AVAILABLE.getStatus());
            phoneNumber.setUpdateDate(new Date());
            phoneNumberRepository.save(phoneNumber);
        });

        String message = "Released phone numbers:" + holdPhoneNumbers.size();
        logger.info(message);

    }

}
