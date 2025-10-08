package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import java.util.List;
import java.util.UUID;

public interface FindMyRestaurantsUseCase {
    List<Restaurant> execute(UUID ownerId);
}
