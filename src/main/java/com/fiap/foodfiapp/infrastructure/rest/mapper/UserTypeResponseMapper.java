package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserTypeResponseDTO;

public class UserTypeResponseMapper {
    private UserTypeResponseMapper() {
    }

    public static UserTypeResponseDTO toDTO(UserType userType) {
        return new UserTypeResponseDTO(
                userType.getUuid(),
                userType.getName()
        );
    }
}
