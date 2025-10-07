package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;

import java.io.IOException;
import java.util.UUID;

public class UpdateMenuItemUseCaseImpl implements UpdateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;

    public MenuItem execute(UUID id, MenuItem menuItemUpdates, String photo) throws IOException {
        var existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        String photoUrl = existingItem.photoUrl();
        if (photo != null && !photo.isEmpty()) {
            // Delete old photo if exists
            if (existingItem.photoUrl() != null) {
                try {
                    String oldFileName = existingItem.photoUrl().substring(existingItem.photoUrl().lastIndexOf('/') + 1);
                    fileStorageRepository.delete(oldFileName);
                } catch (IOException e) {
                    // Log error but continue with update
                    System.err.println("Failed to delete old menu item photo: " + e.getMessage());
                }
            }

            // Store new photo
            String fileName = String.format("%s-%s-%s", existingItem.restaurantId(), id, photo.getOriginalFilename());
            photoUrl = fileStorageRepository.store(photo, fileName);
        }

        // Create new MenuItem with updated values but preserve creation date
        var updatedItem = new MenuItem(
                existingItem.id(),
                menuItemUpdates.name(),
                menuItemUpdates.description(),
                menuItemUpdates.price(),
                menuItemUpdates.localOnly(),
                photoUrl,
                existingItem.restaurantId(),
                existingItem.createdAt(),
                existingItem.updatedAt()
        );

        return menuItemRepository.save(updatedItem);
    }
}
