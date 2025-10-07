package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.DeleteRestaurantUseCase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteRestaurantUseCaseImpl implements DeleteRestaurantUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public DeleteRestaurantUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public void execute(UUID id) {
        this.restaurantRepositoryGateway.deleteRestaurant(id);
    }
}
