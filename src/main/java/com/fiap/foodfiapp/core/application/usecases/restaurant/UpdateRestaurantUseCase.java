package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.restaurant.Restaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.UpdateRestaurant;

public interface UpdateRestaurantUseCase {
    Restaurant update(UpdateRestaurant updateRestaurant);
}
