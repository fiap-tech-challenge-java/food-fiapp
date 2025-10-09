// src/main/java/com/fiap/foodfiapp/core/application/usecases/menuitem/impl/UpdateMenuItemUseCaseImpl.java
package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.validator.MenuItemValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class UpdateMenuItemUseCaseImpl implements UpdateMenuItemUseCase {
    private static final Logger logger = LoggerFactory.getLogger(UpdateMenuItemUseCaseImpl.class);
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public UpdateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, FileStorageRepository fileStorageRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.menuItemRepository = menuItemRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MenuItem execute(UUID authenticatedUserId, UUID id, String name, String description, Double price, Boolean availableForInStoreOnly, FileUploadRequest photo) throws IOException {
        // Validate individual fields that are being updated
        if (name != null) {
            MenuItemValidator.validateName(name);
        }
        if (price != null) {
            MenuItemValidator.validatePrice(price);
        }
        if (description != null) {
            MenuItemValidator.validateDescription(description);
        }

        var existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));

        if (authenticatedUserId == null) {
            throw new UnauthorizedAccessException("Authenticated user not found");
        }

        // Check if user is ADMIN
        User user = userRepository.findById(authenticatedUserId).orElse(null);
        boolean isAdmin = false;
        if (user != null && user.getUserType() != null) {
            String userTypeName = user.getUserType().getName();
            isAdmin = "ADMIN".equalsIgnoreCase(userTypeName);
        }

        Restaurant restaurant = restaurantRepository.findById(existingItem.getRestaurantId());

        // ADMIN can update any menu item, others only their own restaurant's items
        if (!isAdmin && (restaurant == null || restaurant.getUserOwnerId() == null || !authenticatedUserId.equals(restaurant.getUserOwnerId()))) {
            throw new UnauthorizedAccessException("Only the restaurant owner or ADMIN can update this menu item");
        }

        String photoUrl = existingItem.getPhotoUrl();
        if (photo != null && photo.content() != null) {
            String originalFilename = photo.originalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String fileName = String.format("%s-%s%s", restaurant.getId(), id, extension);

            // Upload new photo first
            try (var is = photo.content()) {
                photoUrl = fileStorageRepository.store(is, photo.size(), photo.contentType(), fileName);
            }
            
            // Only delete old photo after successful upload
            if (existingItem.getPhotoUrl() != null && !existingItem.getPhotoUrl().isBlank()) {
                try {
                    String oldFileName = existingItem.getPhotoUrl().substring(existingItem.getPhotoUrl().lastIndexOf('/') + 1);
                    fileStorageRepository.delete(oldFileName);
                } catch (IOException e) {
                    logger.warn("Failed to delete old menu item photo: {}", e.getMessage());
                }
            }
        }

        // Atualiza a entidade existente com os novos dados (somente se não forem null)
        if (name != null) {
            existingItem.setName(name);
        }
        if (description != null) {
            existingItem.setDescription(description);
        }
        if (price != null) {
            existingItem.setPrice(price);
        }
        if (availableForInStoreOnly != null) {
            existingItem.setLocalOnly(availableForInStoreOnly);
        }
        if (photoUrl != null && !photoUrl.equals(existingItem.getPhotoUrl())) {
            existingItem.setPhotoUrl(photoUrl);
        }

        return menuItemRepository.save(existingItem);
    }
}