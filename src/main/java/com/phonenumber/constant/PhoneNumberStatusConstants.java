package com.phonenumber.constant;

public enum PhoneNumberStatusConstants {
    AVAILABLE("Available"),
    LOCKED("Locked"),

    HOLD("Hold"),

    SOLD("Sold"),

    SOLD_ACTIVE("Sold_active");

    private final String status;

    PhoneNumberStatusConstants(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
