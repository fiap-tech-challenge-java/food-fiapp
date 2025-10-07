package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class DeleteMenuItemUseCaseImpl implements DeleteMenuItemUseCase {
    private static final Logger logger = LoggerFactory.getLogger(DeleteMenuItemUseCaseImpl.class);
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
                logger.warn("Failed to delete menu item photo, continuing with menu item deletion: {}", e.getMessage());
            }
        }

        menuItemRepository.deleteById(id);
    }
}