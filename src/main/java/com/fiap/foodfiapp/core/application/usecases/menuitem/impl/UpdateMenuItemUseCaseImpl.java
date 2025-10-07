package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class UpdateMenuItemUseCaseImpl implements UpdateMenuItemUseCase {
    private static final Logger logger = LoggerFactory.getLogger(UpdateMenuItemUseCaseImpl.class);
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;

    public UpdateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, FileStorageRepository fileStorageRepository) {
        this.menuItemRepository = menuItemRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public MenuItem execute(UUID id, MenuItem menuItemUpdates, FileUploadRequest photo) throws IOException {
        var existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));

        String photoUrl = existingItem.getPhotoUrl();
        if (photo != null && photo.content() != null) {
            if (existingItem.getPhotoUrl() != null && !existingItem.getPhotoUrl().isBlank()) {
                try {
                    String oldFileName = existingItem.getPhotoUrl().substring(existingItem.getPhotoUrl().lastIndexOf('/') + 1);
                    fileStorageRepository.delete(oldFileName);
                } catch (IOException e) {
                    logger.warn("Failed to delete old menu item photo: {}", e.getMessage());
                }
            }

            String originalFilename = photo.originalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String fileName = String.format("%s-%s%s", existingItem.getRestaurantId(), id, extension);

            photoUrl = fileStorageRepository.store(photo.content(), photo.size(), photo.contentType(), fileName);
        }

        existingItem.setName(menuItemUpdates.getName());
        existingItem.setDescription(menuItemUpdates.getDescription());
        existingItem.setPrice(menuItemUpdates.getPrice());
        existingItem.setLocalOnly(menuItemUpdates.isLocalOnly());
        existingItem.setPhotoUrl(photoUrl);

        return menuItemRepository.save(existingItem);
    }
}