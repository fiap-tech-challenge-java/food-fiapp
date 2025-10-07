package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantByNameUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.UUID;

public class FindRestaurantByNameUseCaseImpl implements FindRestaurantByNameUseCase {
    private final RestaurantRepository restaurantRepository;

    public FindRestaurantByNameUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant execute(String name, UUID userId) {
        return this.restaurantRepository.findByNameAndUser(name, userId);
    }
}
