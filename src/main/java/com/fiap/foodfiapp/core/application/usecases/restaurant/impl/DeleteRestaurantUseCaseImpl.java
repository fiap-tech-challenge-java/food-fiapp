package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.DeleteRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;

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
    public void execute(UUID id) {
        // Busca o restaurante; se não existir, não faz nada (idempotente)
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant == null) {
            return;
        }

        // Exclusão lógica do restaurante
        restaurantRepository.delete(id);

        // Exclusão lógica dos itens do cardápio associados
        List<MenuItem> items = menuItemRepository.findAllByRestaurantId(id);
        for (MenuItem item : items) {
            item.setIsActive(false);
            menuItemRepository.save(item);
        }

        // Exclusão lógica dos endereços associados ao restaurante
        List<Address> addresses = addressRepository.findByOwner(id, AddressOwnerTypeEnum.RESTAURANT.getDescription());
        for (Address address : addresses) {
            address.setIsActive(false);
            addressRepository.save(address, id, AddressOwnerTypeEnum.RESTAURANT.getDescription());
        }
    }
}
