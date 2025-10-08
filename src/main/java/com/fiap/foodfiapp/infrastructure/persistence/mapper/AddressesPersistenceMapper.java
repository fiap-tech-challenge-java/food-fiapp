package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressesPersistenceMapper {

    AddressesPersistenceMapper INSTANCE = Mappers.getMapper(AddressesPersistenceMapper.class);

    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "ownerType", ignore = true)
    AddressesEntity toEntity(Addresses addresses);

    Addresses toDomain(AddressesEntity addressesEntity);

    List<Addresses> toDomainList(List<AddressesEntity> addressEntities);
}