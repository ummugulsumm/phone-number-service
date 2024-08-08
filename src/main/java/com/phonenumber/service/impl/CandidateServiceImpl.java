package com.phonenumber.service.impl;

import com.phonenumber.model.CandidateModel;
import com.phonenumber.repository.CandidateRepository;
import com.phonenumber.service.CandidateService;
import org.springframework.stereotype.Service;


@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
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
}
