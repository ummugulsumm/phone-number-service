package com.phonenumber.service;

import com.phonenumber.model.CandidateModel;



public interface CandidateService {
    CandidateModel createCandidate(String phoneNumberId, String firstName, String lastName, String motherName, String fatherName, String tcNo, String birthDate);
}
