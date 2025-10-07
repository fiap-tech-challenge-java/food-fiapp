package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.UUID;

public interface ChangeOwnerRestaurantUseCase {
    Restaurant execute(UUID restaurantId, UUID newOwnerId);
}
