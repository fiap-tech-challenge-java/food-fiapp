package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantPersistenceMapper {

    RestaurantPersistenceMapper INSTANCE = Mappers.getMapper(RestaurantPersistenceMapper.class);

    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "description", source = "description") // ADICIONE ESTA LINHA
    RestaurantEntity toEntity(Restaurant restaurant);

    @Mapping(target = "address", ignore = true) // Endereço é carregado sob demanda
    Restaurant toDomain(RestaurantEntity restaurantEntity);
}