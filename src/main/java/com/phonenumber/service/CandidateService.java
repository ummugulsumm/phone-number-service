package com.phonenumber.service;

import com.phonenumber.dto.CandidateDto;
import com.phonenumber.dto.CandidateRequestDto;



public interface CandidateService {
    CandidateDto createCandidate(String phoneNumberId, CandidateRequestDto request);

    CandidateDto addDelivery(String candidateId, String deliveryType, String address);
}
