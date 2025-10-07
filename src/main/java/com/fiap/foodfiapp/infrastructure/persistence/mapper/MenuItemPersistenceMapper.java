package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.infrastructure.persistence.entity.MenuItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuItemPersistenceMapper {

    MenuItemPersistenceMapper INSTANCE = Mappers.getMapper(MenuItemPersistenceMapper.class);

    @Mapping(source = "restaurantId", target = "restaurant.id")
    MenuItemEntity toEntity(MenuItem menuItem);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    MenuItem toDomain(MenuItemEntity menuItemEntity);

    List<MenuItem> toDomainList(List<MenuItemEntity> menuItemEntities);
}