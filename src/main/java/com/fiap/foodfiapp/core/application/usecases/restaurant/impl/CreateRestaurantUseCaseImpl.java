package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.Restaurant;
import org.springframework.stereotype.Service;

@Service
public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {

    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public CreateRestaurantUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public Restaurant executar(CreateRestaurant createRestaurant) {
        this.restaurantRepositoryGateway.createRestaurant(createRestaurant);
        return null;
    }
}
