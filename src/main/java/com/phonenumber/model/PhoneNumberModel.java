package com.phonenumber.model;

import com.phonenumber.constant.PhoneNumberStatusConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberModel {

    @Id
    private String phoneNumberId;

    private String phoneNumber;

    private String status = PhoneNumberStatusConstants.AVAILABLE.getStatus();

    private Date updateDate = new Date();
}
