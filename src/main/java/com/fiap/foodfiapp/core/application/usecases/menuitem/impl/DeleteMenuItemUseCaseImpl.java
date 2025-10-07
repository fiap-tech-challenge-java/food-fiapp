package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;

import java.io.IOException;
import java.util.UUID;

public class DeleteMenuItemUseCaseImpl implements DeleteMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;

    public DeleteMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, FileStorageRepository fileStorageRepository) {
        this.menuItemRepository = menuItemRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public void execute(UUID id) {
        var menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));

        if (menuItem.getPhotoUrl() != null && !menuItem.getPhotoUrl().isBlank()) {
            try {
                String fileName = menuItem.getPhotoUrl().substring(menuItem.getPhotoUrl().lastIndexOf('/') + 1);
                fileStorageRepository.delete(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete menu item photo", e);
            }
        }

        menuItemRepository.deleteById(id);
    }
}