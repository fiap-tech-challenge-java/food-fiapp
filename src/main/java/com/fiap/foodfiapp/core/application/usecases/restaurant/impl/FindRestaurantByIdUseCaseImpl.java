package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantByIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.UUID;

public class FindRestaurantByIdUseCaseImpl implements FindRestaurantByIdUseCase {
    private final RestaurantRepository restaurantRepository;

    public FindRestaurantByIdUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant execute(UUID id) {
        return this.restaurantRepository.findById(id);
    }
}
