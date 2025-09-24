package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.exception.FileStorageException;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteMenuItemUseCase {
    private static final Logger logger = LoggerFactory.getLogger(DeleteMenuItemUseCase.class);
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;

    @Transactional
    public void execute(UUID id) {
        var menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        if (menuItem.photoUrl() != null) {
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
