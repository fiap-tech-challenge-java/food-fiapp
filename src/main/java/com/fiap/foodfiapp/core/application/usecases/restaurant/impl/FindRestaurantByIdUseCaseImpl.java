package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantByIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindRestaurantByIdUseCaseImpl implements FindRestaurantByIdUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public FindRestaurantByIdUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public Restaurant execute(UUID id) {
        return this.restaurantRepositoryGateway.findById(id);
    }
}
