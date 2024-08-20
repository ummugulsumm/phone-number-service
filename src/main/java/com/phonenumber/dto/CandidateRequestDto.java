package com.phonenumber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequestDto {

    private String firstName;
    private String lastName;
    private String motherName;
    private String fatherName;
    private String tcNo;
    private String birthDate;
}
