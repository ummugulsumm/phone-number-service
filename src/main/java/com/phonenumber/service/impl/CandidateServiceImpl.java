package com.phonenumber.service.impl;

import com.phonenumber.constant.DeliveryTypeConstants;
import com.phonenumber.exception.CandidateNotFoundException;
import com.phonenumber.model.CandidateModel;
import com.phonenumber.repository.CandidateRepository;
import com.phonenumber.service.CandidateService;
import com.phonenumber.service.PhoneNumberService;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final PhoneNumberService phoneNumberService;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PhoneNumberService phoneNumberService) {
        this.candidateRepository = candidateRepository;
        this.phoneNumberService = phoneNumberService;

    }

    @Override
    public CandidateModel createCandidate(String phoneNumberId, String firstName, String lastName, String motherName, String fatherName, String tcNo, String birthDate) {
        CandidateModel candidate = new CandidateModel();
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setMotherName(motherName);
        candidate.setFatherName(fatherName);
        candidate.setTcNo(tcNo);
        candidate.setPhoneNumberId(phoneNumberId);
        candidate.setBirthdate(birthDate);
        return candidateRepository.save(candidate);
    }

    @Override
    public CandidateModel addDelivery(String candidateId, String deliveryType, String address) {
        CandidateModel candidate = candidateRepository.findById(candidateId)
                .orElseThrow(CandidateNotFoundException::new);
        candidate.setAddress(address);
        if(deliveryType.equals("HOME")) {
            candidate.setDeliveryType(DeliveryTypeConstants.HOME.getDeliveryType());
        } else if(deliveryType.equals("STORE")) {
            candidate.setDeliveryType((DeliveryTypeConstants.STORE.getDeliveryType()));
        }
        candidate.setUpdateDate(new Date());
        phoneNumberService.updateStatusSold(candidate.getPhoneNumberId());
        return candidateRepository.save(candidate);
    }
}
