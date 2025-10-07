package com.fiap.foodfiapp.infrastructure.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressOwnerTypeEnum {
    USER("USER"),
    RESTAURANT("RESTAURANT");

    private final String description;
}
