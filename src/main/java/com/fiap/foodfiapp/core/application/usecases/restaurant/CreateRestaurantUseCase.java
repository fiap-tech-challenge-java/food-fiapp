// src/main/java/com/fiap/foodfiapp/core/application/usecases/restaurant/CreateRestaurantUseCase.java
package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;

public interface CreateRestaurantUseCase {
    Restaurant execute(Restaurant restaurant);
}