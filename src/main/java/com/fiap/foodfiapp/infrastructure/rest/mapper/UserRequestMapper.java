package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.core.domain.entity.User;

public class UserRequestMapper {
    private UserRequestMapper() {}

    public static User toEntity(UserRequestDTO dto) {
        return new User(null, dto.getName(), dto.getEmail(), dto.getPassword());
    }
}

