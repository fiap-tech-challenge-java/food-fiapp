package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.FindMyRestaurantsUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;

import java.util.List;
import java.util.UUID;

public class FindMyRestaurantsUseCaseImpl implements FindMyRestaurantsUseCase {
    private final RestaurantRepository restaurantRepository;

    public FindMyRestaurantsUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> execute(UUID ownerId) {
        return restaurantRepository.findAllByUserId(ownerId);
    }
}
