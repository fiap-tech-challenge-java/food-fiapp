package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantPersistenceMapper {

    RestaurantPersistenceMapper INSTANCE = Mappers.getMapper(RestaurantPersistenceMapper.class);

    @Mapping(target = "menuItems", ignore = true) // A lista de menu é gerenciada separadamente
    RestaurantEntity toEntity(Restaurant restaurant);

    Restaurant toDomain(RestaurantEntity restaurantEntity);
}