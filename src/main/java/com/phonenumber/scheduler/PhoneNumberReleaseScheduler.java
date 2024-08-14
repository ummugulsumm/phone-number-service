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
public class PhoneNumberReleaseScheduler {



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
            phoneNumber.setContactPhoneNumber("");
            phoneNumber.setUpdateDate(new Date());
            phoneNumberRepository.save(phoneNumber);
        });

        log.info("Released phone numbers:" + holdPhoneNumbers.size());

    }

}
