package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest; // <- Nossa abstração
import com.fiap.foodfiapp.core.application.usecases.menuitem.CreateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;

import java.io.IOException;
import java.util.UUID;

public class CreateMenuItemUseCaseImpl implements CreateMenuItemUseCase {
    // ... construtor ...
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileStorageRepository fileStorageRepository;

    public CreateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, FileStorageRepository fileStorageRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.fileStorageRepository = fileStorageRepository;
    }


    @Override
    public MenuItem execute(MenuItem menuItem, FileUploadRequest photo) throws IOException {
        if (!restaurantRepository.findByIdActive(menuItem.restaurantId())) {
            throw new IllegalArgumentException("Restaurant not found or is inactive.");
        }

        String photoUrl = null;
        if (photo != null && photo.content() != null) {
            String originalFilename = photo.originalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String fileName = String.format("%s-%s%s", menuItem.restaurantId(), UUID.randomUUID(), extension);

            photoUrl = fileStorageRepository.store(
                    photo.content(),
                    photo.size(),
                    photo.contentType(),
                    fileName
            );
        }

        var itemToSave = menuItem.withPhotoUrl(photoUrl);
        return menuItemRepository.save(itemToSave);
    }
}