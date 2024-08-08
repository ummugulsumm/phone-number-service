package com.phonenumber.constant;

public enum PhoneNumberTypeConstants {

    GOLD("GOLD", 10000),
    SILVER("SILVER", 5000),
    BRONZE("BRONZE", 1000),
    PLAIN("PLAIN", 0);

    private final String specialPhoneNumberType;

    private final int specialPhoneNumberPrice;

    PhoneNumberTypeConstants(String specialPhoneNumberType, int specialPhoneNumberPrice) {
        this.specialPhoneNumberType = specialPhoneNumberType;
        this.specialPhoneNumberPrice = specialPhoneNumberPrice;
    }


    public int getSpecialPhoneNumberPrice() {
        return specialPhoneNumberPrice;
    }

    public String getSpecialPhoneNumberType() {
        return specialPhoneNumberType;
    }



}
