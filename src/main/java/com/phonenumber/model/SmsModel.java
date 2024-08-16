package com.phonenumber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsModel {

    @Id
    private String smsId;

    private String specialPhoneNumber;

    private String contactPhoneNumber;

    private String message;

    private LocalDateTime sendAt;


}
