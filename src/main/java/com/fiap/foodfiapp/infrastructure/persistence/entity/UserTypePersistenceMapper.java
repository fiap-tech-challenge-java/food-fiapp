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
        return new UserTypeEntity(
                domain.getUuid(), // Para entidades novas, isso será null e o Hibernate gerará o UUID
                domain.getName()
        );
    }
}
