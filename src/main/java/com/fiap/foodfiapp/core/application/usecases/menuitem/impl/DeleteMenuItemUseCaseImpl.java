package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;

import java.io.IOException;
import java.util.UUID;

public class DeleteMenuItemUseCaseImpl implements DeleteMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;
    private final RestaurantRepository restaurantRepository;

    public DeleteMenuItemUseCaseImpl(MenuItemRepository menuItemRepository,
                                   FileStorageRepository fileStorageRepository,
                                   RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void execute(UUID authenticatedUserId, UUID itemId) {
        // 1. Buscar o item do menu
        var menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException("Item do menu não encontrado."));

        // 2. Buscar o restaurante ao qual o item pertence
        var restaurant = restaurantRepository.findById(menuItem.getRestaurantId());
        if (restaurant == null) {
            throw new BusinessException("Restaurante não encontrado.");
        }

        // 3. Validar se o usuário autenticado é o dono do restaurante
        if (!authenticatedUserId.equals(restaurant.getUserOwnerId())) {
            throw new UnauthorizedAccessException("Permissão negada. Você só pode deletar itens do menu de seus próprios restaurantes.");
        }

        // 4. Deletar a foto se existir
        if (menuItem.getPhotoUrl() != null && !menuItem.getPhotoUrl().isBlank()) {
            try {
                String fileName = menuItem.getPhotoUrl().substring(menuItem.getPhotoUrl().lastIndexOf('/') + 1);
                fileStorageRepository.delete(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete menu item photo", e);
            }
        }

        // 5. Deletar o item do menu
        menuItemRepository.deleteById(itemId);
    }
}