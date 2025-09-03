package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.core.domain.entity.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UserRequestMapper {
    private UserRequestMapper() {}

    public static User toEntity(UserRequestDTO dto) {
        return new User(
                UUID.randomUUID(),
                dto.getName(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getCpf(),
                null,
                null,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                dto.getPassword()
        );
    }
}

