package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuItem execute(UUID id, MenuItem menuItemUpdates) {
        var existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        // Create new MenuItem with updated values but preserve restaurant and creation date
        var updatedItem = new MenuItem(
                existingItem.id(),
                menuItemUpdates.name(),
                menuItemUpdates.description(),
                menuItemUpdates.price(),
                menuItemUpdates.localOnly(),
                menuItemUpdates.photoBase64() != null ? menuItemUpdates.photoBase64() : existingItem.photoBase64(),
                existingItem.restaurantId(),
                existingItem.createdAt(),
                existingItem.updatedAt()
        );

        return menuItemRepository.save(updatedItem);
    }
}
