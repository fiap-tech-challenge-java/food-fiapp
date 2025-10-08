package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.FindAllPublicRestaurantsUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;

import java.util.List;
import java.util.UUID;

public class FindAllPublicRestaurantsUseCaseImpl implements FindAllPublicRestaurantsUseCase {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final AddressRepository addressRepository;

    public FindAllPublicRestaurantsUseCaseImpl(RestaurantRepository restaurantRepository,
                                             MenuItemRepository menuItemRepository,
                                             AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Restaurant> execute(UUID userId) {

        //verificar se o userId é um customer

        List<Restaurant> restaurants = restaurantRepository.findAllActive();

        // Iterate over each restaurant to populate menu items and addresses
        for (Restaurant restaurant : restaurants) {
            // Find and set menu items for this restaurant
            var menuItems = menuItemRepository.findAllByRestaurantId(restaurant.getId());
            restaurant.setMenuItems(menuItems);

            // Find and set addresses for this restaurant
            var addresses = addressRepository.findByOwner(restaurant.getId(), AddressOwnerTypeEnum.RESTAURANT.getDescription());
            // Note: Since Restaurant entity has single Addresses field, we'll set the first address if available
            if (!addresses.isEmpty()) {
                restaurant.setAddress(addresses.get(0));
            }
        }

        return restaurants;
    }
}
