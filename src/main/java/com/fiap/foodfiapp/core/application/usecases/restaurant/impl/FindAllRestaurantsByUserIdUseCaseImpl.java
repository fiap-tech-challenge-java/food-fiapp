package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindAllRestaurantsByUserIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FindAllRestaurantsByUserIdUseCaseImpl implements FindAllRestaurantsByUserIdUseCase {
    private final RestaurantRepositoryGateway restaurantRepositoryGateway;

    public FindAllRestaurantsByUserIdUseCaseImpl(RestaurantRepositoryGateway restaurantRepositoryGateway) {
        this.restaurantRepositoryGateway = restaurantRepositoryGateway;
    }

    @Override
    public List<Restaurant> execute(UUID id) {
       return this.restaurantRepositoryGateway.findAllByUserId(id);
    }
}
