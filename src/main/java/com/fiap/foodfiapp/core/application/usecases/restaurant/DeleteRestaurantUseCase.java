package com.fiap.foodfiapp.core.application.usecases.restaurant;

import java.util.UUID;

public interface DeleteRestaurantUseCase {
    void execute(UUID authenticatedUserId, UUID restaurantId);
}
