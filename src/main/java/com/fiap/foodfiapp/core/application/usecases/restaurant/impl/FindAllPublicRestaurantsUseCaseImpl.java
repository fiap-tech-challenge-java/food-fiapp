package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.FindAllPublicRestaurantsUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;

import java.util.List;

public class FindAllPublicRestaurantsUseCaseImpl implements FindAllPublicRestaurantsUseCase {

    private final RestaurantRepository restaurantRepository;

    public FindAllPublicRestaurantsUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> execute() {
        return restaurantRepository.findAllActive();
    }
}

