package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemSpringDataRepository extends JpaRepository<MenuItemEntity, UUID> {
    List<MenuItemEntity> findAllByRestaurantId(UUID restaurantId);
}