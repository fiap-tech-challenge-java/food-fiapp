package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.restaurant.Restaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.UpdateRestaurant;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public UpdateRestaurantUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public Restaurant update(UpdateRestaurant updateRestaurant) {
        return this.restaurantRepositoryGateway.updateRestaurant(updateRestaurant);
    }

    @Override
    public Restaurant changeOwner(UUID restaurantId, UUID newOwnerId) {
        return this.restaurantRepositoryGateway.changeOwner(restaurantId, newOwnerId);
    }
}
