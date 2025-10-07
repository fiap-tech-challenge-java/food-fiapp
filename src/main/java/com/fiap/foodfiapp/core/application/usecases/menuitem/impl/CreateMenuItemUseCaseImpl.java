// src/main/java/com/fiap/foodfiapp/core/application/usecases/menuitem/impl/CreateMenuItemUseCaseImpl.java
package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.CreateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;

import java.io.IOException;
import java.util.UUID;

public class CreateMenuItemUseCaseImpl implements CreateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileStorageRepository fileStorageRepository;

    public CreateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, FileStorageRepository fileStorageRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public MenuItem execute(UUID restaurantId, String name, String description, Double price, Boolean availableForInStoreOnly, FileUploadRequest photo) throws IOException {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant not found or is inactive.");
        }

        // A entidade agora é criada aqui, dentro da camada de aplicação.
        MenuItem menuItem = new MenuItem(null, name, description, price,
                availableForInStoreOnly != null && availableForInStoreOnly,
                null, restaurantId, null, null);

        String photoUrl = null;
        if (photo != null && photo.content() != null) {
            String originalFilename = photo.originalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            // Gera um nome de ficheiro único para evitar conflitos
            String fileName = String.format("%s-%s%s", restaurantId, UUID.randomUUID(), extension);

            photoUrl = fileStorageRepository.store(
                    photo.content(),
                    photo.size(),
                    photo.contentType(),
                    fileName
            );
        }

        menuItem.setPhotoUrl(photoUrl);
        return menuItemRepository.save(menuItem);
    }
}