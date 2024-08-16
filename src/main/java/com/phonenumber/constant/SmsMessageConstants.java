package com.phonenumber.constant;

public enum SmsMessageConstants {

    HOLD("Your number %s has been reserved as per your request."),
    SOLD("Your order for %s number has been successfully completed."),
    RELEASE("Your reservation for the number %s has been canceled due to inactivity."),
    DELIVERED("You can start using the number %s now.");

    private final String message;

    SmsMessageConstants(String message) {
        this.message = message;
    }

    public String getMessage(String phoneNumber) {
        return String.format(message, phoneNumber);
    }
}
