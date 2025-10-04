package com.fiap.foodfiapp.infrastructure.rest.dto.restaurant;

import java.util.UUID;

public record CreateRestaurantResponseDTO(
        UUID id,
        String name,
        String cuisineType,
        String openingHours,
        Boolean active,
        UUID userOwnerId

) {
}
