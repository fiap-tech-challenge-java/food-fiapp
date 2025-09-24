package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.MenuItemsApi;
import com.fiap.foodfiapp.core.application.usecases.menuitem.CreateMenuItemUseCase;
import com.fiap.foodfiapp.core.application.usecases.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodfiapp.core.application.usecases.menuitem.GetMenuItemByIdUseCase;
import com.fiap.foodfiapp.core.application.usecases.menuitem.GetMenuItemsByRestaurantUseCase;
import com.fiap.foodfiapp.core.application.usecases.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.FileStorageException;
import com.fiap.foodfiapp.model.MenuItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MenuItemController implements MenuItemsApi {
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetMenuItemsByRestaurantUseCase getMenuItemsByRestaurantUseCase;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    @Override
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(UUID restaurantId) {
        List<MenuItem> menuItems = getMenuItemsByRestaurantUseCase.execute(restaurantId);
        List<MenuItemResponse> responses = menuItems.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> createMenuItem(
            UUID restaurantId,
            String name,
            String description,
            Double price,
            Boolean availableForInStoreOnly,
            MultipartFile photo
    ) {
        try {
            MenuItem menuItem = new MenuItem(
                    null,
                    name,
                    description,
                    BigDecimal.valueOf(price),
                    availableForInStoreOnly != null ? availableForInStoreOnly : false,
                    null,
                    restaurantId,
                    null,
                    null
            );

            MenuItem createdItem = createMenuItemUseCase.execute(menuItem, photo);
            return ResponseEntity.ok(toResponse(createdItem));
        } catch (IOException e) {
            throw new FileStorageException("Failed to store menu item photo", e);
        }
    }

    @Override
    public ResponseEntity<MenuItemResponse> getMenuItem(UUID restaurantId, UUID itemId) {
        return getMenuItemByIdUseCase.execute(itemId)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            UUID restaurantId,
            UUID itemId,
            String name,
            String description,
            Double price,
            Boolean availableForInStoreOnly,
            MultipartFile photo
    ) {
        try {
            MenuItem menuItemUpdate = new MenuItem(
                    itemId,
                    name,
                    description,
                    BigDecimal.valueOf(price),
                    availableForInStoreOnly != null ? availableForInStoreOnly : false,
                    null,
                    restaurantId,
                    null,
                    null
            );

            MenuItem updatedItem = updateMenuItemUseCase.execute(itemId, menuItemUpdate, photo);
            return ResponseEntity.ok(toResponse(updatedItem));
        } catch (IOException e) {
            throw new FileStorageException("Failed to store menu item photo", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Void> deleteMenuItem(UUID restaurantId, UUID itemId) {
        deleteMenuItemUseCase.execute(itemId);
        return ResponseEntity.noContent().build();
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        var response = new MenuItemResponse();
        response.setId(menuItem.id());
        response.setName(menuItem.name());
        response.setDescription(menuItem.description());
        response.setPrice(menuItem.price().doubleValue());
        response.setAvailableForInStoreOnly(menuItem.localOnly());
        response.setPhotoUrl(menuItem.photoUrl());
        response.setRestaurantId(menuItem.restaurantId());
        return response;
    }
}
