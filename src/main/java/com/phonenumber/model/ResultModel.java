package com.phonenumber.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultModel {

    private String result;
    private String resultCode;
    private String resultDesc;
}
