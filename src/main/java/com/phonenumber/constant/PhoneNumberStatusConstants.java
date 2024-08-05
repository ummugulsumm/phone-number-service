package com.phonenumber.constant;

public enum PhoneNumberStatusConstants {
    AVAILABLE("Available"),
    LOCKED("Locked"),

    HOLD("Hold"),

    SOLD("Sold");

    private final String status;

    PhoneNumberStatusConstants(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
