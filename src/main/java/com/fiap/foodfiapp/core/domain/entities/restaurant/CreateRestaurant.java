package com.fiap.foodfiapp.core.domain.entities.restaurant;

import java.util.UUID;

public record CreateRestaurant(
        String name,
        String cuisineType,
        String openingHours,
        UUID userOwnerId
) {
}
