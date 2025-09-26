package com.fiap.foodfiapp.infrastructure.rest.dto;

import java.util.UUID;

public record CreateRestaurantResponseDTO(
        UUID id,
        String name,
        String cuisineType,
        String openingHours,
        Boolean active,
        UUID userId
) {
}
