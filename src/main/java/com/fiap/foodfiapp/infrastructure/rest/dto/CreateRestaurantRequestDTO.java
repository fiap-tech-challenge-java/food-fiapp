package com.fiap.foodfiapp.infrastructure.rest.dto;

import java.util.UUID;

public record CreateRestaurantRequestDTO(
        String name,
        String cuisineType,
        String openingHours,
        UUID idUserOwner
) {
}
