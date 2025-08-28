package com.fiap.foodfiapp.infrastructure.persistence.entity;

import com.fiap.foodfiapp.core.domain.entity.User;

public class UserPersistenceMapper {
    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        return new UserEntity(user.id(), user.name(), user.email(), user.password());
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword());
    }
}

