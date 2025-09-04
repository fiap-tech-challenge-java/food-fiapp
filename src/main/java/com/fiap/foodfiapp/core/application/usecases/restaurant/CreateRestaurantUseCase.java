package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.Restaurant;

public interface CreateRestaurantUseCase {
    Restaurant executar(CreateRestaurant createRestaurant);
}
