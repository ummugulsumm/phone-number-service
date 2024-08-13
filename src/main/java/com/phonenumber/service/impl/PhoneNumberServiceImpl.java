package com.phonenumber.service.impl;

import com.phonenumber.constant.PhoneNumberResultConstants;
import com.phonenumber.constant.PhoneNumberStatusConstants;
import com.phonenumber.exception.PhoneNumberLimitExceededException;
import com.phonenumber.exception.PhoneNumberNotFoundException;
import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.model.ResponseModel;
import com.phonenumber.model.ResultModel;
import com.phonenumber.repository.PhoneNumberRepository;
import com.phonenumber.service.PhoneNumberService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@Service
@AllArgsConstructor
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public ResponseModel getAvailablePhoneNumbers() {
        ResultModel result = new ResultModel(
                PhoneNumberResultConstants.SUCCESS_RESULT.getResult(),
                String.valueOf(HttpStatus.OK.value()),
                PhoneNumberResultConstants.SUCCESS_MESSAGE.getResult()
        );
        return new ResponseModel(result, phoneNumberRepository.findAll());
    }

    @Override
    @Transactional
    public ResponseModel getAvailablePhoneNumbersByCount(Integer count) {

        List<PhoneNumberModel> phoneNumbers = findAvailablePhoneNumbers();

        phoneNumbers = phoneNumbers.subList(0, count);

        updateStatus(phoneNumbers, PhoneNumberStatusConstants.LOCKED);

        ResultModel result = new ResultModel(
                PhoneNumberResultConstants.SUCCESS_RESULT.getResult(),
                String.valueOf(HttpStatus.OK.value()),
                PhoneNumberResultConstants.SUCCESS_MESSAGE.getResult()
        );

        return new ResponseModel(result, phoneNumbers);

    }

    @Override
    public ResponseModel getAvailablePhoneNumbersByDigit(String digit) {

        List<PhoneNumberModel> phoneNumbersByDigit;

        for (int i = 0; i <= digit.length(); i++) {
            phoneNumbersByDigit = findPhoneNumbersByDigit(digit);
            if (!phoneNumbersByDigit.isEmpty()) {

                updateStatus(phoneNumbersByDigit, PhoneNumberStatusConstants.LOCKED);

                ResultModel result = new ResultModel(
                        PhoneNumberResultConstants.SUCCESS_RESULT.getResult(),
                        String.valueOf(HttpStatus.OK.value()),
                        PhoneNumberResultConstants.SUCCESS_MESSAGE.getResult()
                );
                return new ResponseModel(result, phoneNumbersByDigit);
            }
            digit = digit.substring(1);
        }

        throw new PhoneNumberNotFoundException();
    }


    @Override
    public void updateStatusSold(String phoneNumberId) {
        PhoneNumberModel phoneNumber = phoneNumberRepository.findById(phoneNumberId)
                .orElseThrow(PhoneNumberNotFoundException::new);
        phoneNumber.setStatus(PhoneNumberStatusConstants.SOLD.getStatus());
        phoneNumber.setUpdateDate(new Date());
        phoneNumberRepository.save(phoneNumber);
    }

    @Override
    public ResponseEntity<Void> addContactPhoneNumber(String phoneNumberId, String contactPhoneNumber) {

        PhoneNumberModel phoneNumber = phoneNumberRepository.findById(phoneNumberId)
                .orElseThrow(PhoneNumberNotFoundException::new);

        String type = phoneNumber.getSpecialPhoneNumberType();

        Query query = new Query();
        query.addCriteria(Criteria.where("contactPhoneNumber").is(contactPhoneNumber)
                .and("specialPhoneNumberType").is(type));

        int count = (int) mongoTemplate.count(query, PhoneNumberModel.class);

        if(count < 3) {
            phoneNumber.setContactPhoneNumber(contactPhoneNumber);
            phoneNumber.setStatus(PhoneNumberStatusConstants.HOLD.getStatus());
            phoneNumber.setUpdateDate(new Date());
            phoneNumberRepository.save(phoneNumber);
            return new ResponseEntity<>(OK);
        } else {
            throw new PhoneNumberLimitExceededException();
        }
    }


    @Override
    public PhoneNumberModel createPhoneNumber(PhoneNumberModel newPhoneNumber) {
        return phoneNumberRepository.save(newPhoneNumber);
    }

    @Override
    public void deletePhoneNumber(String id) {
        phoneNumberRepository.deleteById(id);
    }

    @Override
    public void updateStatusAvailable() {
        List<PhoneNumberModel> phoneNumbers = phoneNumberRepository.findAll();
        updateStatus(phoneNumbers, PhoneNumberStatusConstants.AVAILABLE);
    }


    private void updateStatus(List<PhoneNumberModel> phoneNumbers, PhoneNumberStatusConstants phoneNumberStatusConstants) {

        phoneNumbers.forEach(phoneNumber -> {
            phoneNumber.setStatus(phoneNumberStatusConstants.getStatus());
            phoneNumber.setUpdateDate(new Date());
        });

        phoneNumberRepository.saveAll(phoneNumbers);
    }

    private List<PhoneNumberModel> findAvailablePhoneNumbers() {

        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(PhoneNumberStatusConstants.AVAILABLE.getStatus()));

        return mongoTemplate.find(query, PhoneNumberModel.class);
    }

    private List<PhoneNumberModel> findPhoneNumbersByDigit(String digit) {

        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("Available")
                .and("phoneNumber").regex(digit + "$"));
        query.limit(4);
        return mongoTemplate.find(query, PhoneNumberModel.class);
    }


}
