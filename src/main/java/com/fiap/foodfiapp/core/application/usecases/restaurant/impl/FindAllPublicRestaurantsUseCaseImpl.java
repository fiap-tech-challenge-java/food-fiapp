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


        List<Restaurant> restaurants = restaurantRepository.findAllActive();

        for (Restaurant restaurant : restaurants) {
            var menuItems = menuItemRepository.findAllByRestaurantId(restaurant.getId());
            restaurant.setMenuItems(menuItems);

            var addresses = addressRepository.findByOwner(restaurant.getId(), AddressOwnerTypeEnum.RESTAURANT.getDescription());
            if (!addresses.isEmpty()) {
                restaurant.setAddress(addresses.get(0));
            }
        }

        return java.util.Collections.unmodifiableList(new java.util.ArrayList<>(restaurants));
    }
}
