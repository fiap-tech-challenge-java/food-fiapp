package com.fiap.foodfiapp.core.domain.entity;

import java.util.UUID;

public record UserType(
        UUID id,
        String name
) {}