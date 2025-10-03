package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entities.restaurant.*;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.rest.dto.restaurant.CreateRestaurantRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.restaurant.CreateRestaurantResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.restaurant.UpdateRestaurantRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    CreateRestaurant mapToCreateRestaurant(CreateRestaurantRequestDTO createRestaurantRequestDTO);

    Restaurant mapToRestaurant(RestaurantEntity restaurantEntity);
    List<Restaurant> mapToListRestaurant(List<RestaurantEntity> restaurantEntities);

    CreatedRestaurant mapToCreatedRestaurant(RestaurantEntity restaurantEntity);

    CreateRestaurantResponseDTO mapToCreateRestaurantResponseDTO(CreatedRestaurant createdRestaurant);

    UpdateRestaurant mapToUpdateRestaurant(UpdateRestaurantRequestDTO updateRestaurantRequestDTO);
}
