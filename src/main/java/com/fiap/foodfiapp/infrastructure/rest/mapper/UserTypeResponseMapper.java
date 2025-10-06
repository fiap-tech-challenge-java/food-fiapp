package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.model.UserTypeResponse;

public class UserTypeResponseMapper {
    private UserTypeResponseMapper() {
    }

    public static UserTypeResponse toDTO(UserType userType) {
        return new UserTypeResponse()
                .uuid(userType.getUuid())
                .name(userType.getName())
                .isActive(userType.isActive())
                .createdAt(userType.getCreatedAt())
                .updatedAt(userType.getUpdatedAt());
    }
}
