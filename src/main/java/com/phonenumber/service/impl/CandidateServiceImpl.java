package com.phonenumber.service.impl;

import com.phonenumber.model.CandidateModel;
import com.phonenumber.repository.CandidateRepository;
import com.phonenumber.service.CandidateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
}
