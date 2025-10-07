package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.restaurant.*;
import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.*;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new CreateRestaurantUseCaseImpl(restaurantRepository);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new DeleteRestaurantUseCaseImpl(restaurantRepository);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new UpdateRestaurantUseCaseImpl(restaurantRepository);
    }

    @Bean
    public FindAllRestaurantsByUserIdUseCase findAllRestaurantsByUserIdUseCase(RestaurantRepository restaurantRepository) {
        return new FindAllRestaurantsByUserIdUseCaseImpl(restaurantRepository);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantRepository restaurantRepository) {
        return new FindRestaurantByIdUseCaseImpl(restaurantRepository);
    }

    @Bean
    public FindRestaurantByNameUseCase findRestaurantByNameUseCase(RestaurantRepository restaurantRepository) {
        return new FindRestaurantByNameUseCaseImpl(restaurantRepository);
    }

    @Bean
    public ChangeOwnerRestaurantUseCase changeOwnerRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new ChangeOwnerRestaurantUseCaseImpl(restaurantRepository);
    }
}