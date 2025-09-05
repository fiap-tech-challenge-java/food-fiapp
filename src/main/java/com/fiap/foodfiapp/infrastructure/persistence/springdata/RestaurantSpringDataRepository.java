package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.core.domain.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantSpringDataRepository extends JpaRepository<Restaurant, UUID> {
}
