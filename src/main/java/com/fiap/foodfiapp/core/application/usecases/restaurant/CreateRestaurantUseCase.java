package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.CreatedRestaurant;

public interface CreateRestaurantUseCase {
    CreatedRestaurant execute(CreateRestaurant createRestaurant);
}
