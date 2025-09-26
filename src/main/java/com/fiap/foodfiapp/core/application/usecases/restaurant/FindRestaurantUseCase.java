package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.restaurant.Restaurant;

import java.util.List;
import java.util.UUID;

public interface FindRestaurantUseCase {
    Restaurant findById(UUID id);
    Restaurant findByName(String name, UUID userId);
    List<Restaurant> findAllByUserId(UUID id);
}
