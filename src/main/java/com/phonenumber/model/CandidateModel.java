package com.phonenumber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateModel {


    @Id
    private String candidateId;
    private String phoneNumberId;
    private String firstName;
    private String lastName;
    private String motherName;
    private String fatherName;
    private String tcNo;
    private String birthdate;
    private String address;
    private String deliveryType;
    private Date updateDate = new Date();

}
