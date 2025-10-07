package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressPersistenceMapper.class, UserTypePersistenceMapper.class})
public interface UserPersistenceMapper {

    UserPersistenceMapper INSTANCE = Mappers.getMapper(UserPersistenceMapper.class);

    @Mapping(source = "addresses", target = "addressesList")
    UserEntity toEntity(User user);

    @Mapping(source = "addressesList", target = "addresses")
    User toDomain(UserEntity userEntity);
}