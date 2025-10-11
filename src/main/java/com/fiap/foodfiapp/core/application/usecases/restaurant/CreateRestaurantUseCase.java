package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import java.util.UUID;

public interface CreateRestaurantUseCase {
    Restaurant execute(UUID userId, Restaurant restaurant);
}