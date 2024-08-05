package com.phonenumber.repository;

import com.phonenumber.model.PhoneNumberModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;


public interface PhoneNumberRepository extends MongoRepository<PhoneNumberModel, String> {
    List<PhoneNumberModel> findByStatusAndUpdateDateBefore(String status, Date date);
}
