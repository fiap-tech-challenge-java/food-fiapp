package com.fiap.foodfiapp.core.domain.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String email,
        String cpf,
        String login,
        List<Address> address,
        OffsetDateTime updatedAt,
        OffsetDateTime createdAt,
        UserType role,
        boolean active,
        String password
) {}