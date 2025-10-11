package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.DeleteRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;

import java.util.List;
import java.util.UUID;

public class DeleteRestaurantUseCaseImpl implements DeleteRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final AddressRepository addressRepository;

    public DeleteRestaurantUseCaseImpl(RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository,
            AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void execute(UUID authenticatedUserId, UUID restaurantId) {
        if (restaurantId == null) {
            return;
        }
        
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) {
            return;
        }

        if (!authenticatedUserId.equals(restaurant.getUserOwnerId())) {
            throw new UnauthorizedAccessException("Permissão negada. Você só pode deletar seus próprios restaurantes.");
        }

        restaurantRepository.delete(restaurantId);

        List<MenuItem> items = menuItemRepository.findAllByRestaurantId(restaurantId);
        for (MenuItem item : items) {
            item.setIsActive(false);
            menuItemRepository.save(item);
        }

        List<Addresses> addresses = addressRepository.findByOwner(restaurantId,
                AddressOwnerTypeEnum.RESTAURANT.getDescription());
        for (Addresses address : addresses) {
            address.setIsActive(false);
            addressRepository.save(address, restaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription());
        }
    }
}
