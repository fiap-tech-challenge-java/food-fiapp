package com.fiap.foodfiapp.core.domain.enums;

public enum AddressOwnerTypeEnum {
    USER("USER"),
    RESTAURANT("RESTAURANT");

    private final String description;

    AddressOwnerTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
