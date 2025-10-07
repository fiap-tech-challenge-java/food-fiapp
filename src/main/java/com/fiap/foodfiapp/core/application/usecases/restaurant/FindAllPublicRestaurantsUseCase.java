package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.List;

public interface FindAllPublicRestaurantsUseCase {
    List<Restaurant> execute();
}

