package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserTypePersistenceMapper.class})
public interface UserPersistenceMapper {

    UserPersistenceMapper INSTANCE = Mappers.getMapper(UserPersistenceMapper.class);

    UserEntity toEntity(User user);

    User toDomain(UserEntity userEntity);
}