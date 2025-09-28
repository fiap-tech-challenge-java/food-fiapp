package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.restaurant.Restaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.UpdateRestaurant;

import java.util.UUID;

public interface UpdateRestaurantUseCase {
    Restaurant update(UpdateRestaurant updateRestaurant);
    Restaurant changeOwner(UUID restaurantId, UUID newOwnerId);
}
