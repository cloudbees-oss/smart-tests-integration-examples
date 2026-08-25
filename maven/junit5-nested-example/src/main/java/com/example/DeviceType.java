package com.example;

public enum DeviceType {
    PHONE(1),
    TABLET(2),
    LAPTOP(3);

    private final int value;

    DeviceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DeviceType getByValue(int value) {
        for (DeviceType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
