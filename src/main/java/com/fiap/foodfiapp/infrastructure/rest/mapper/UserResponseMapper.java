package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserResponseDTO;

public class UserResponseMapper {
    private UserResponseMapper() {}

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.id(), user.name(), user.email());
    }
}

