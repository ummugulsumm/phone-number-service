package com.phonenumber.repository;
import com.phonenumber.model.SmsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmsRepository extends MongoRepository<SmsModel, String> {
}
