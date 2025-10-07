package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindAllRestaurantsByUserIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.List;
import java.util.UUID;

public class FindAllRestaurantsByUserIdUseCaseImpl implements FindAllRestaurantsByUserIdUseCase {
    private final RestaurantRepository restaurantRepository;

    public FindAllRestaurantsByUserIdUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> execute(UUID id) {
       return this.restaurantRepository.findAllByUserId(id);
    }
}
