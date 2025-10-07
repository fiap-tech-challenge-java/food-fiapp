package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.MenuItemsApi;
import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.FileStorageException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.MenuItemMapper;
import com.fiap.foodfiapp.model.MenuItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final FindAllMenuItemsUseCase findAllMenuItemsUseCase;
    private final FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    private final MenuItemMapper menuItemMapper = MenuItemMapper.INSTANCE;

    @Override
    public ResponseEntity<MenuItemResponse> createMenuItem(UUID restaurantId, String name, String description, Double price, Boolean availableForInStoreOnly, MultipartFile photo) {
        try {
            MenuItem menuItem = new MenuItem(null, name, description, price,
                    availableForInStoreOnly != null && availableForInStoreOnly,
                    null, restaurantId, null, null);

            FileUploadRequest fileUploadRequest = null;
            if (photo != null && !photo.isEmpty()) {
                fileUploadRequest = new FileUploadRequest(photo.getInputStream(), photo.getSize(),
                        photo.getContentType(), photo.getOriginalFilename());
            }

            MenuItem createdItem = createMenuItemUseCase.execute(menuItem, fileUploadRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(menuItemMapper.toMenuItemResponse(createdItem));

        } catch (IOException e) {
            throw new FileStorageException("Failed to process menu item photo", e);
        }
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(UUID restaurantId, UUID itemId) {
        deleteMenuItemUseCase.execute(itemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MenuItemResponse> getMenuItem(UUID restaurantId, UUID itemId) {
        return findMenuItemByIdUseCase.execute(itemId)
                .map(menuItemMapper::toMenuItemResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(UUID restaurantId) {
        List<MenuItem> menuItems = findAllMenuItemsUseCase.execute(restaurantId);
        return ResponseEntity.ok(menuItemMapper.toMenuItemResponseList(menuItems));
    }

    @Override
    public ResponseEntity<MenuItemResponse> updateMenuItem(UUID restaurantId, UUID itemId, String name, String description, Double price, Boolean availableForInStoreOnly, MultipartFile photo) {
        try {
            MenuItem menuItemUpdate = new MenuItem(itemId, name, description, price,
                    availableForInStoreOnly != null && availableForInStoreOnly,
                    null, restaurantId, null, null);

            FileUploadRequest fileUploadRequest = null;
            if (photo != null && !photo.isEmpty()) {
                fileUploadRequest = new FileUploadRequest(photo.getInputStream(), photo.getSize(),
                        photo.getContentType(), photo.getOriginalFilename());
            }

            MenuItem updatedItem = updateMenuItemUseCase.execute(itemId, menuItemUpdate, fileUploadRequest);
            return ResponseEntity.ok(menuItemMapper.toMenuItemResponse(updatedItem));
        } catch (IOException e) {
            throw new FileStorageException("Failed to process menu item photo on update", e);
        }
    }
}