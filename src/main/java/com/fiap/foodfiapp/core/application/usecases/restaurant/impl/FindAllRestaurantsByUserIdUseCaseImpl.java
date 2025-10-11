package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindAllRestaurantsByUserIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.List;
import java.util.UUID;

public class FindAllRestaurantsByUserIdUseCaseImpl implements FindAllRestaurantsByUserIdUseCase {
    private final RestaurantRepository restaurantRepository;

    public FindAllRestaurantsByUserIdUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> execute(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        return this.restaurantRepository.findAllByUserId(id);
    }
}
