package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.Restaurant;
import com.fiap.foodfiapp.infrastructure.rest.dto.CreateRestaurantRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private static final RestaurantMapper RESTAURANT_MAPPER = RestaurantMapper.INSTANCE;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody CreateRestaurantRequestDTO restaurant) {
        CreateRestaurant createRestaurant = RESTAURANT_MAPPER.mapToCreateRestaurant(restaurant);

        this.createRestaurantUseCase.executar(createRestaurant);
        return null;
    }
}
