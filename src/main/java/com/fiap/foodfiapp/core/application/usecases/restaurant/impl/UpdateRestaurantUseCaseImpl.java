package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import org.springframework.stereotype.Service;

@Service
public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public UpdateRestaurantUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public Restaurant execute(Restaurant updateRestaurant) {
        return this.restaurantRepositoryGateway.updateRestaurant(updateRestaurant);
    }
}
