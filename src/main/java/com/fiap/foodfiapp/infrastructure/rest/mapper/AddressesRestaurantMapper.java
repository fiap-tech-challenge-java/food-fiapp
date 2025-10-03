package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;
import com.fiap.foodfiapp.infrastructure.rest.dto.addressesrestaurant.CreateAddressesRestaurantRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.addressesrestaurant.CreateAddressesRestaurantResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressesRestaurantMapper {
    AddressesRestaurantMapper INSTANCE = Mappers.getMapper(AddressesRestaurantMapper.class);

    CreateAddressesRestaurant mapToCreateAddressesRestaurant(CreateAddressesRestaurantRequestDTO createAddressesRestaurantResponseDTO);
    CreateAddressesRestaurantResponseDTO mapToCreateAddressesRestaurantResponseDTO(CreatedAddressesRestaurant createdAddressesRestaurant);
}
