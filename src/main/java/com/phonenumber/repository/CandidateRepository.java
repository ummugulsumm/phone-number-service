package com.phonenumber.repository;

import com.phonenumber.model.CandidateModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateRepository extends MongoRepository<CandidateModel, String> {
}
