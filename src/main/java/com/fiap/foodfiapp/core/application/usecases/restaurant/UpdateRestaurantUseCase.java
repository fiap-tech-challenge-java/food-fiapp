package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entities.Restaurant;
import com.fiap.foodfiapp.core.domain.entities.UpdateRestaurant;

public interface UpdateRestaurantUseCase {
    Restaurant update(UpdateRestaurant updateRestaurant);
}
