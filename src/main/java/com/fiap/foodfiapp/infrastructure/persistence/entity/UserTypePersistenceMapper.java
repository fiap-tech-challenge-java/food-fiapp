package com.fiap.foodfiapp.infrastructure.persistence.entity;

import com.fiap.foodfiapp.core.domain.entity.UserType;

public class UserTypePersistenceMapper {
    private UserTypePersistenceMapper() {
    }

    public static UserType toDomain(UserTypeEntity entity) {
        return new UserType(
                entity.getUuid(),
                entity.getName()
        );
    }

    public static UserTypeEntity toEntity(UserType domain) {
        return UserTypeEntity.builder()
                .uuid(domain.getUuid())
                .name(domain.getName())
                .build();
    }

    public static void updateEntityFromDomain(UserTypeEntity entity, UserType domain) {
        entity.setName(domain.getName());
    }
}
