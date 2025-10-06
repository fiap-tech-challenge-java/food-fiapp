package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemEntity, UUID> {
    List<MenuItemEntity> findAllByRestaurantId(UUID restaurantId);
}
