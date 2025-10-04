package com.fiap.foodfiapp.infrastructure.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressesOwnerTypeEnum {
    USER("USER"),
    RESTAURANT("RESTAURANT");

    private final String description;
}
