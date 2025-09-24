package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final FileStorageRepository fileStorageRepository;

    @Transactional
    public void execute(UUID id) {
        var menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        // Delete photo from MinIO if exists
        if (menuItem.photoUrl() != null) {
            try {
                String fileName = menuItem.photoUrl().substring(menuItem.photoUrl().lastIndexOf('/') + 1);
                fileStorageRepository.delete(fileName);
            } catch (IOException e) {
                // Log error but continue with deletion
                System.err.println("Failed to delete menu item photo: " + e.getMessage());
            }
        }

        menuItemRepository.deleteById(id);
    }
}
