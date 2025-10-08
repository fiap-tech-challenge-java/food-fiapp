package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.restaurant.*;
import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.*;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantRepository restaurantRepository, UserRepository userRepository, AddressRepository addressRepository) {
        return new CreateRestaurantUseCaseImpl(restaurantRepository, userRepository, addressRepository);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepository restaurantRepository,
                                                           MenuItemRepository menuItemRepository,
                                                           AddressRepository addressRepository) {
        return new DeleteRestaurantUseCaseImpl(restaurantRepository, menuItemRepository, addressRepository);
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
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantRepository restaurantRepository, AddressRepository addressRepository) {
        return new FindRestaurantByIdUseCaseImpl(restaurantRepository, addressRepository);
    }

    @Bean
    public FindRestaurantByNameUseCase findRestaurantByNameUseCase(RestaurantRepository restaurantRepository) {
        return new FindRestaurantByNameUseCaseImpl(restaurantRepository);
    }

    @Bean
    public ChangeOwnerRestaurantUseCase changeOwnerRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new ChangeOwnerRestaurantUseCaseImpl(restaurantRepository);
    }

    @Bean
    public FindAllPublicRestaurantsUseCase findAllPublicRestaurantsUseCase(RestaurantRepository restaurantRepository,
                                                                           MenuItemRepository menuItemRepository,
                                                                           AddressRepository addressRepository) {
        return new FindAllPublicRestaurantsUseCaseImpl(restaurantRepository, menuItemRepository, addressRepository);
    }

    @Bean
    public FindMyRestaurantsUseCase findMyRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        return new FindMyRestaurantsUseCaseImpl(restaurantRepository);
    }

    @Bean
    public ValidateRestaurantOwnershipUseCase validateRestaurantOwnershipUseCase(RestaurantRepository restaurantRepository) {
        return new ValidateRestaurantOwnershipUseCaseImpl(restaurantRepository);
    }
}