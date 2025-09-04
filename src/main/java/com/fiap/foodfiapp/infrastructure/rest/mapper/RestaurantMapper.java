package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.infrastructure.rest.dto.CreateRestaurantRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    CreateRestaurant mapToCreateRestaurant(CreateRestaurantRequestDTO createRestaurantRequestDTO);
}
