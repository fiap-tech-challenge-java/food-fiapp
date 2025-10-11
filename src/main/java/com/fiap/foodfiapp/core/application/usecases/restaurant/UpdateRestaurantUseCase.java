package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import java.util.UUID;

public interface UpdateRestaurantUseCase {
    Restaurant execute(UUID authenticatedUserId, UUID restaurantId, Restaurant restaurantUpdates);
}