package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.FindMenuItemByIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;

import java.util.Optional;
import java.util.UUID;

public class FindMenuItemByIdUseCaseImpl implements FindMenuItemByIdUseCase {
    private final MenuItemRepository menuItemRepository;

    public Optional<MenuItem> execute(UUID id) {
        return menuItemRepository.findById(id);
    }
}
