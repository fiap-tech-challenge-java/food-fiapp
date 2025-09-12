package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserTypeRequestDTO;

public class UserTypeRequestMapper {
    private UserTypeRequestMapper() {
    }

    public static UserType toEntity(UserTypeRequestDTO dto) {
        UserType userType = new UserType();
        userType.setName(dto.getName());
        return userType;
    }
}
