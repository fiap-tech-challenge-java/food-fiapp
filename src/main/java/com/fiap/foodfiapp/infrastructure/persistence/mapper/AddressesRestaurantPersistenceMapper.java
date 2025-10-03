package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressesRestaurantPersistenceMapper {
    AddressesRestaurantPersistenceMapper INSTANCE = Mappers.getMapper(AddressesRestaurantPersistenceMapper.class);

    AddressesEntity mapToAddressesEntity(CreateAddressesRestaurant createdAddressesRestaurant);
}
