package com.phonenumber.constant;

public enum SmsMessageConstants {

    HOLD("Your number %s has been reserved as per your request."),
    SOLD("Your order for %s number has been successfully completed."),
    RELEASE_HOLD("Your reservation for number %s has been canceled due to inactivity."),
    DELIVERED("You can start using number %s now."),
    RELEASE_SOLD("Your order for number %s has been canceled as it has not been received");

    private final String message;

    SmsMessageConstants(String message) {
        this.message = message;
    }

    public String getMessage(String phoneNumber) {
        return String.format(message, phoneNumber);
    }
}
