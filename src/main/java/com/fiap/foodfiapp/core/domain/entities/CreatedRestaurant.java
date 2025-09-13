package com.fiap.foodfiapp.core.domain.entities;

import java.util.UUID;

public record CreatedRestaurant(
        UUID id,
        String name,
        String cuisineType,
        String openingHours,
        UUID userId
) {
}
