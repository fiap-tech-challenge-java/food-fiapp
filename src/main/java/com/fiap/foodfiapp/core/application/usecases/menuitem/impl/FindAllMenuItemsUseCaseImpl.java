package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.FindAllMenuItemsUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;

import java.util.List;
import java.util.UUID;

public class FindAllMenuItemsUseCaseImpl implements FindAllMenuItemsUseCase {
    private final MenuItemRepository menuItemRepository;

    public List<MenuItem> execute(UUID restaurantId) {
        return menuItemRepository.findAllByRestaurantId(restaurantId);
    }
}
