package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.CreateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;


import java.io.IOException;
import java.util.UUID;

public class CreateMenuItemUseCaseImpl implements CreateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileStorageRepository fileStorageRepository;

    public MenuItem execute(MenuItem menuItem, String photo) throws IOException {
        if (!restaurantRepository.findByIdActive(menuItem.restaurantId())) {
            throw new IllegalArgumentException("Restaurant not found");
        }

        String photoUrl = null;
        if (photo != null && !photo.isBlank()) {
            String fileName = String.format("%s-%s-%s", menuItem.restaurantId(), UUID.randomUUID(), photo.substring(photo.lastIndexOf('.') + 1));
            photoUrl = fileStorageRepository.store(photo, fileName);
        }

        var itemToSave = menuItem.withPhotoUrl(photoUrl);
        return menuItemRepository.save(itemToSave);
    }
}
