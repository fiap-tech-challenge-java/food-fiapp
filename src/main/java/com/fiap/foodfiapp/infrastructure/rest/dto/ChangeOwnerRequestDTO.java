package com.fiap.foodfiapp.infrastructure.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangeOwnerRequestDTO(
        @NotNull(message = "The new owner's ID is mandatory.")
        UUID newOwnerId
) {
}
