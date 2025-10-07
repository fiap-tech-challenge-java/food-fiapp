package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.CreateUserRequest;

import java.time.OffsetDateTime;

public class CreateUserRequestMapper {
    private static final boolean DEFAULT_ACTIVE = true;

    private CreateUserRequestMapper() {
    }

    public static User toEntity(CreateUserRequest dto) {
        UserType userType = new UserType();
        userType.setUuid(dto.getUserTypeUuid());
        boolean isActive = dto.getActive() == null ? DEFAULT_ACTIVE : dto.getActive();
        return new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getCpf(),
                AddressMapper.toEntity(dto.getAddresses()),
                userType,
                isActive,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                dto.getPassword()
        );
    }


}
