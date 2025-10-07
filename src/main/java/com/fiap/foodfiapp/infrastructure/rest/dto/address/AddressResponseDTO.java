package com.fiap.foodfiapp.infrastructure.rest.dto.address;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        String publicPlace,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        Boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}