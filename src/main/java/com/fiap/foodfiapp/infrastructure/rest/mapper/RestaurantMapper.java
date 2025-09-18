package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entities.*;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    CreateRestaurant mapToCreateRestaurant(CreateRestaurantRequestDTO createRestaurantRequestDTO);

    Restaurant mapToRestaurant(RestaurantEntity restaurantEntity);

    CreatedRestaurant mapToCreatedRestaurant(RestaurantEntity restaurantEntity);

    CreateRestaurantResponseDTO mapToCreateRestaurantResponseDTO(CreatedRestaurant createdRestaurant);

    UpdateRestaurant mapToUpdateRestaurant(UpdateRestaurantRequestDTO updateRestaurantRequestDTO);
}
