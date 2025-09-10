package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.core.domain.entity.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UserRequestMapper {
    private UserRequestMapper() {
    }

    public static User toEntity(UserRequestDTO dto) {
        UserType userType = new UserType();
        userType.setUuid(dto.getUserTypeUuid());
        return new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getCpf(),
                AdressRequestMapper.toEntity(dto.getAddresses()),
                userType,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                dto.getPassword()
        );
    }
}

