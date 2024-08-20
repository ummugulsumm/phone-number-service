package com.phonenumber.service.impl;

import com.phonenumber.constant.DeliveryTypeConstants;
import com.phonenumber.dto.CandidateDto;
import com.phonenumber.dto.CandidateRequestDto;
import com.phonenumber.exception.CandidateNotFoundException;
import com.phonenumber.model.CandidateModel;
import com.phonenumber.repository.CandidateRepository;
import com.phonenumber.service.CandidateService;
import com.phonenumber.service.PhoneNumberService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final PhoneNumberService phoneNumberService;

    private final ModelMapper modelMapper;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PhoneNumberService phoneNumberService, ModelMapper modelMapper) {
        this.candidateRepository = candidateRepository;
        this.phoneNumberService = phoneNumberService;
        this.modelMapper = modelMapper;

    }

    @Override
    public CandidateDto createCandidate(String phoneNumberId, CandidateRequestDto request) {
        CandidateModel candidate = modelMapper.map(request, CandidateModel.class);
                       candidate.setPhoneNumberId(phoneNumberId);
        return modelMapper.map(candidateRepository.save(candidate), CandidateDto.class);
    }

    @Override
    public CandidateDto addDelivery(String candidateId, String deliveryType, String address) {
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
        return modelMapper.map(candidateRepository.save(candidate), CandidateDto.class);
    }
}
