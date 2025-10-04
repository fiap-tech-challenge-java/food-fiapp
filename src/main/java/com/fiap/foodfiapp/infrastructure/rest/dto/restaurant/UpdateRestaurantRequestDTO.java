package com.fiap.foodfiapp.infrastructure.rest.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateRestaurantRequestDTO(
        @NotNull(message = "The restaurant's ID is mandatory.")
        UUID id,

        @NotBlank(message = "The name of the restaurant is mandatory.")
        String name,

        @NotBlank(message = "The cuisine type of the restaurant is mandatory.")
        String cuisineType,

        @NotBlank(message = "The time that the restaurant opens is mandatory.")
        String openingHours,

        @NotNull(message = "The user's ID is mandatory.")
        UUID userOwnerId
) {
}
