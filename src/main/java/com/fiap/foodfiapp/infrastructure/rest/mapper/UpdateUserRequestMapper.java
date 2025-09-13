package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.UpdateUserRequest;

import java.time.OffsetDateTime;

public class UpdateUserRequestMapper {
    private UpdateUserRequestMapper() {
    }

    public static User toEntity(UpdateUserRequest dto) {
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

