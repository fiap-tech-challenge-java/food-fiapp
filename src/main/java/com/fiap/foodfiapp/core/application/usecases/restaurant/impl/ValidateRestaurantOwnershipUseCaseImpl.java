package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.ValidateRestaurantOwnershipUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.RestaurantNotFoundException;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class ValidateRestaurantOwnershipUseCaseImpl implements ValidateRestaurantOwnershipUseCase {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ValidateRestaurantOwnershipUseCaseImpl(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean execute(UUID userId, UUID restaurantId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getUserType() != null) {
            String userTypeName = user.getUserType().getName();
            if ("ADMIN".equalsIgnoreCase(userTypeName)) {
                return true;
            }
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            throw new RestaurantNotFoundException("id", restaurantId.toString());
        }

        return restaurant.getUserOwnerId().equals(userId);
    }
}