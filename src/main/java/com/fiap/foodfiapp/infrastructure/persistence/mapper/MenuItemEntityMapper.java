package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.infrastructure.persistence.entity.MenuItemEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuItemEntityMapper {
    public MenuItem toDomain(MenuItemEntity entity) {
        return new MenuItem(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.isLocalOnly(),
            entity.getPhotoUrl(),
            entity.getRestaurant().getId(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().toLocalDateTime() : null,
            entity.getUpdatedAt() != null ? entity.getUpdatedAt().toLocalDateTime() : null
        );
    }

    public MenuItemEntity toEntity(MenuItem domain) {
        var entity = new MenuItemEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        entity.setDescription(domain.description());
        entity.setPrice(domain.price());
        entity.setLocalOnly(domain.localOnly());
        entity.setPhotoUrl(domain.photoUrl());

        if (domain.restaurantId() != null) {
            var restaurant = new RestaurantEntity();
            restaurant.setId(domain.restaurantId());
            entity.setRestaurant(restaurant);
        }

        return entity;
    }
}
