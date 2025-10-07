package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import java.util.List;
import java.util.UUID;

public interface FindAllRestaurantsByUserIdUseCase {
    List<Restaurant> execute(UUID userId);
}

