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

    // CONSTRUTOR ADICIONADO
    public DeleteMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, FileStorageRepository fileStorageRepository) {
        this.menuItemRepository = menuItemRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override // Adicionada anotação para garantir conformidade com a interface
    public void execute(UUID id) {
        var menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));

        if (menuItem.photoUrl() != null && !menuItem.photoUrl().isBlank()) {
            try {
                String fileName = menuItem.photoUrl().substring(menuItem.photoUrl().lastIndexOf('/') + 1);
                fileStorageRepository.delete(fileName);
            } catch (IOException e) {
                logger.warn("Failed to delete menu item photo, continuing with menu item deletion: {}", e.getMessage());
            }
        }

        menuItemRepository.deleteById(id);
    }
}