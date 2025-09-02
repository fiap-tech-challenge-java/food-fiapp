package com.fiap.foodfiapp.core.domain.entity;

import java.util.UUID;

public record Address(
        UUID id,
        String publicPlace,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode
) {}