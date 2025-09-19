package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.entities.restaurant.*;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepositoryGateway {
    CreatedRestaurant createRestaurant(CreateRestaurant createRestaurant);
    Restaurant findById(UUID id);
    Restaurant findByName(String name, UUID userId);
    List<Restaurant> findAllByUserId(UUID userId);
    Restaurant updateRestaurant(UpdateRestaurant updateRestaurant);
}
