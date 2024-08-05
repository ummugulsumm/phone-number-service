package com.phonenumber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseModel {
    private ResultModel result;
    private List<PhoneNumberModel> phoneNumbers;

    public ResponseModel(ResultModel result) {
        this.result = result;
        this.phoneNumbers = Collections.emptyList();
    }
}
