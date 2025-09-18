package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.UserTypeRequest;

public class UserTypeRequestMapper {
    private UserTypeRequestMapper() {
    }

    public static UserType toEntity(UserTypeRequest dto) {
        UserType userType = new UserType();
        userType.setName(dto.getName());
        return userType;
    }
}
