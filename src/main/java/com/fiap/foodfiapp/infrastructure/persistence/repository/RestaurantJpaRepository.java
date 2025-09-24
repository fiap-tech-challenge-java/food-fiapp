package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, UUID> {
}
