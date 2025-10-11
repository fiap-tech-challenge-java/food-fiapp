package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.addresses.*;
import com.fiap.foodfiapp.core.application.usecases.addresses.impl.*;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AddressesUseCaseConfig {

    @Bean
    public CreateAddressesUseCase createAddressUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository,
            ValidateOwnerUseCase validateOwnerUseCase) {
        return new CreateAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);
    }


    @Bean
    public UpdateAddressesUseCase updateAddressUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository,
            ValidateOwnerUseCase validateOwnerUseCase) {
        return new UpdateAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);
    }


    @Bean
    public DeleteAddressesUseCase deleteAddressUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository,
            ValidateOwnerUseCase validateOwnerUseCase) {
        return new DeleteAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);
    }


    @Bean
    public FindAddressesByOwnerUseCase findAddressByOwnerUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository) {
        return new FindAddressesByOwnerUseCaseImpl(addressRepository);
    }


    @Bean
    public ValidateOwnerUseCase validateOwnerUseCase(UserRepository userRepository,
            RestaurantRepository restaurantRepository) {
        return new ValidateOwnerUseCaseImpl(userRepository, restaurantRepository);
    }
}