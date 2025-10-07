package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserTypePersistenceMapper {

    UserTypePersistenceMapper INSTANCE = Mappers.getMapper(UserTypePersistenceMapper.class);

    UserTypeEntity toEntity(UserType userType);

    @Mapping(source = "isActive", target = "active")
    UserType toDomain(UserTypeEntity userTypeEntity);
}