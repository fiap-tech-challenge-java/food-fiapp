package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.ChangeOwnerRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeOwnerRestaurantUseCaseImpl implements ChangeOwnerRestaurantUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public ChangeOwnerRestaurantUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }
    @Override
    public Restaurant execute(UUID restaurantId, UUID newOwnerId) {
        return this.restaurantRepositoryGateway.changeOwner(restaurantId, newOwnerId);
    }
}
