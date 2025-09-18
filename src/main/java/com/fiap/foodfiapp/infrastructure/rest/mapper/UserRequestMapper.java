package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.model.UserRequest;

import java.time.OffsetDateTime;

import java.util.UUID;

public class UserRequestMapper {
    private static final boolean DEFAULT_ACTIVE = true;

    private UserRequestMapper() {
    }

    public static User toEntity(UserRequest dto) {
        UserType userType = new UserType();
        userType.setUuid(dto.getUserTypeUuid());
        return new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getCpf(),
                AddressRequestMapper.toEntity(dto.getAddresses()),
                userType,
                dto.getActive(),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                dto.getPassword()
        );
    }


}

