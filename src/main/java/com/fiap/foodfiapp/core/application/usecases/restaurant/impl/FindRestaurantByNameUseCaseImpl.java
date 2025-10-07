package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantByNameUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindRestaurantByNameUseCaseImpl implements FindRestaurantByNameUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public FindRestaurantByNameUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public Restaurant execute(String name, UUID userId) {
        return this.restaurantRepositoryGateway.findByName(name, userId);
    }
}
