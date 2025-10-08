package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.ValidateMenuItemOwnershipUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.MenuItemNotFoundException;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;

import java.util.UUID;

public class ValidateMenuItemOwnershipUseCaseImpl implements ValidateMenuItemOwnershipUseCase {
    private final MenuItemRepository menuItemRepository;

    public ValidateMenuItemOwnershipUseCaseImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public boolean execute(UUID itemId, UUID restaurantId) {
        MenuItem menuItem = menuItemRepository.findById(itemId)
            .orElseThrow(() -> new MenuItemNotFoundException("id", itemId.toString()));

        return menuItem.getRestaurantId().equals(restaurantId);
    }
}
