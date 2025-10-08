package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.ValidateRestaurantOwnershipUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.RestaurantNotFoundException;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;

import java.util.UUID;

public class ValidateRestaurantOwnershipUseCaseImpl implements ValidateRestaurantOwnershipUseCase {
    private final RestaurantRepository restaurantRepository;

    public ValidateRestaurantOwnershipUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public boolean execute(UUID userId, UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new RestaurantNotFoundException("id", restaurantId.toString());
        }

        return restaurant.getUserOwnerId().equals(userId);
    }
}
