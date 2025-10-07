package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressPersistenceMapper {

    AddressPersistenceMapper INSTANCE = Mappers.getMapper(AddressPersistenceMapper.class);

    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "ownerType", ignore = true)
    AddressEntity toEntity(Address address);

    Address toDomain(AddressEntity addressEntity);

    List<Address> toDomainList(List<AddressEntity> addressEntities);
}