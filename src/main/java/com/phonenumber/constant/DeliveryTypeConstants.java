package com.phonenumber.constant;

public enum DeliveryTypeConstants {

    HOME("Home"),
    STORE("Store");

    private final String deliveryType;

    DeliveryTypeConstants(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }



}
