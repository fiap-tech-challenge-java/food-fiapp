package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.ChangeOwnerRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.UUID;

public class ChangeOwnerRestaurantUseCaseImpl implements ChangeOwnerRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public ChangeOwnerRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    @Override
    public Restaurant execute(UUID restaurantId, UUID newOwnerId) {
        if (restaurantId == null) {
            throw new NullPointerException("Restaurant ID cannot be null");
        }
        if (newOwnerId == null) {
            throw new NullPointerException("New owner ID cannot be null");
        }
        
        return this.restaurantRepository.changeOwner(restaurantId, newOwnerId);
    }
}
