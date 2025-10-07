package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.entity.*;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepositoryGateway {
    Restaurant createRestaurant(Restaurant createRestaurant);
    Restaurant findById(UUID id);
    Restaurant findByName(String name, UUID userId);
    List<Restaurant> findAllByUserId(UUID userId);
    Restaurant updateRestaurant(Restaurant updateRestaurant);
    void deleteRestaurant(UUID id);
    Restaurant changeOwner(UUID restaurantId, UUID newOwnerId);
}
