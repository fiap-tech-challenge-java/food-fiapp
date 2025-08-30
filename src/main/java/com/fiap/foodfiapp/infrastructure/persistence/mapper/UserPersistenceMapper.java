package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entities.CreateUser;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPersistenceMapper {
    UserPersistenceMapper INSTANCE = Mappers.getMapper(UserPersistenceMapper.class);

    UserEntity mapToEntity(User user);
    UserEntity mapToEntity(CreateUser user);
    User mapToUser(UserEntity userEntity);
}

