package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;

public class UserTypeRequestMapper {
    private UserTypeRequestMapper() {
    }

    public static UserType toEntity(CreateUserTypeRequest dto) {
        UserType userType = new UserType();
        userType.setName(dto.getName());
        return userType;
    }

    public static UserType toEntity(UpdateUserTypeRequest dto) {
        UserType userType = new UserType();
        userType.setName(dto.getName());
        return userType;
    }
}
