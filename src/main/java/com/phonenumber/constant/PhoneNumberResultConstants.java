package com.phonenumber.constant;

public enum PhoneNumberResultConstants {

    SUCCESS_RESULT("Success"),
    SUCCESS_MESSAGE("Operation successful."),
    FAILED_RESULT("Failed"),
    NOT_FOUND_MESSAGE("No available phone numbers match the search criteria."),
    AUTHENTICATION_FAILED_MESSAGE("Authentication failed!"),
    FORBIDDEN_MESSAGE("Access is forbidden!"),

    INTERNAL_SERVER_MESSAGE("Internal server error!");

    private final String result;

    PhoneNumberResultConstants(String result) { this.result = result; }

    public String getResult() {
        return result;
    }
}
