package com.phonenumber.constant;

public enum DeliveryTypeConstants {

    ADDRESS("Address"),
    STORE("Store");

    private final String deliveryType;

    DeliveryTypeConstants(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }



}
