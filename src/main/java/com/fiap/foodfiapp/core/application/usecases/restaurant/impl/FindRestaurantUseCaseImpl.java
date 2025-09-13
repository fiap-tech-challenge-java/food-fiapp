package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.Restaurant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindRestaurantUseCaseImpl implements FindRestaurantUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public FindRestaurantUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public Restaurant findById(UUID id) {
        return this.restaurantRepositoryGateway.findById(id);
    }

    @Override
    public Restaurant findByName(String name, UUID userId) {
        return this.restaurantRepositoryGateway.findByName(name, userId);
    }

    @Override
    public Restaurant findAllByUserId(UUID id) {
        return null;
    }
}
