package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;

public interface CreateRestaurantUseCase {
    Restaurant execute(java.util.UUID authenticatedUserId, Restaurant createRestaurant);
}
