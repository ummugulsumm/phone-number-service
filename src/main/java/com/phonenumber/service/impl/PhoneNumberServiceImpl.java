package com.phonenumber.service.impl;

import com.phonenumber.constant.PhoneNumberResultConstants;
import com.phonenumber.constant.PhoneNumberStatusConstants;
import com.phonenumber.constant.SmsMessageConstants;
import com.phonenumber.dto.PhoneNumberDto;
import com.phonenumber.exception.PhoneNumberLimitExceededException;
import com.phonenumber.exception.PhoneNumberNotFoundException;
import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.model.ResponseModel;
import com.phonenumber.model.ResultModel;
import com.phonenumber.repository.PhoneNumberRepository;
import com.phonenumber.service.AiService;
import com.phonenumber.service.PhoneNumberService;
import com.phonenumber.service.SmsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    private final MongoTemplate mongoTemplate;
    private final SmsService smsService;

    private final ModelMapper modelMapper;

    @Autowired
    private final AiService aiService;

    @Override
    public ResponseModel getAvailablePhoneNumbers() {
        ResultModel result = new ResultModel(
                PhoneNumberResultConstants.SUCCESS_RESULT.getResult(),
                String.valueOf(HttpStatus.OK.value()),
                PhoneNumberResultConstants.SUCCESS_MESSAGE.getResult()
        );
        List<PhoneNumberModel> phoneNumbers = phoneNumberRepository.findAll();
        List<PhoneNumberDto> dtos = phoneNumbers.stream().map(phoneNumber -> modelMapper.map(phoneNumber, PhoneNumberDto.class)).toList();
        return new ResponseModel(result, dtos);
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


        List<PhoneNumberDto> dtos = phoneNumbers.stream().map(phoneNumber -> modelMapper.map(phoneNumber, PhoneNumberDto.class)).toList();
        return new ResponseModel(result, dtos);

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

                List<PhoneNumberDto> dtos = phoneNumbersByDigit.stream().map(phoneNumber -> modelMapper.map(phoneNumber, PhoneNumberDto.class)).toList();
                return new ResponseModel(result, dtos);
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
        smsService.sendSms(phoneNumber.getPhoneNumber(), phoneNumber.getContactPhoneNumber(), SmsMessageConstants.SOLD.getMessage(phoneNumber.getPhoneNumber()));

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
            smsService.sendSms(phoneNumber.getPhoneNumber(), phoneNumber.getContactPhoneNumber(), SmsMessageConstants.HOLD.getMessage(phoneNumber.getPhoneNumber()));
            return new ResponseEntity<>(OK);
        } else {
            throw new PhoneNumberLimitExceededException();
        }
    }

    @Override
    public String getAiHelp(List<Object> phoneNumbers) {
        StringBuilder prompt = new StringBuilder("I want to change my phone number. Which of these options do you think is the best one to choose? Decide by looking at both the price and the pattern of the number.\n");
        for (Object number : phoneNumbers) {
            prompt.append("\n- ").append(number);
        }
        return aiService.chatGemini(prompt.toString());
    }

    @Override
    public PhoneNumberDto updateStatusSoldActive(String phoneNumberId) {
        PhoneNumberModel phoneNumber = phoneNumberRepository.findById(phoneNumberId)
                .orElseThrow(PhoneNumberNotFoundException::new);
        if(phoneNumber.getStatus().equals(PhoneNumberStatusConstants.SOLD.getStatus())) {
            phoneNumber.setStatus(PhoneNumberStatusConstants.SOLD_ACTIVE.getStatus());
            smsService.sendSms(phoneNumber.getPhoneNumber(), phoneNumber.getContactPhoneNumber(), SmsMessageConstants.DELIVERED.getMessage(phoneNumber.getPhoneNumber()));
            return modelMapper.map(phoneNumberRepository.save(phoneNumber), PhoneNumberDto.class);
        } else {
            throw new PhoneNumberNotFoundException();
        }
    }


    @Override
    public PhoneNumberDto createPhoneNumber(PhoneNumberDto newPhoneNumber) {
        PhoneNumberModel phoneNumber = modelMapper.map(newPhoneNumber, PhoneNumberModel.class);
        return modelMapper.map(phoneNumberRepository.save(phoneNumber), PhoneNumberDto.class);
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
