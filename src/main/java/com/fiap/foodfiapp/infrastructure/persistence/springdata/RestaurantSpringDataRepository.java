package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantSpringDataRepository extends JpaRepository<RestaurantEntity, UUID> {
    Optional<RestaurantEntity> findByUserIdAndNameAndActiveTrue(UUID userId, String name);

    Optional<List<RestaurantEntity>> findAllByUserIdAndActiveTrue(UUID userId);

    Optional<RestaurantEntity> findByIdAndActiveTrue(UUID restaurantId);
}

