package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.restaurant.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.CreatedRestaurant;

public interface CreateRestaurantUseCase {
    CreatedRestaurant execute(CreateRestaurant createRestaurant);
}
