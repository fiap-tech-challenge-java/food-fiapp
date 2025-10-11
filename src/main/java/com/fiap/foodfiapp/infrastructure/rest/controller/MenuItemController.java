package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.MenuItemsApi;
import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.infrastructure.rest.mapper.MenuItemMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.MenuItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MenuItemController implements MenuItemsApi {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final FindAllMenuItemsUseCase findAllMenuItemsUseCase;
    private final ValidateMenuItemOwnershipUseCase validateMenuItemOwnershipUseCase;
    private final AuthenticationService authenticationService;
    private final MenuItemMapper menuItemMapper;

    @Override
    public ResponseEntity<MenuItemResponse> createMenuItem(
            UUID userId,
            UUID restaurantId,
            String name,
            String description,
            Double price,
            Boolean availableForInStoreOnly,
            MultipartFile photo) {

        try {
            // Convert MultipartFile to FileUploadRequest
            FileUploadRequest fileUploadRequest = null;
            if (photo != null && !photo.isEmpty()) {
                fileUploadRequest = new FileUploadRequest(
                    photo.getInputStream(),
                    photo.getSize(),
                    photo.getContentType(),
                    photo.getOriginalFilename()
                );
            }

            var createdMenuItem = createMenuItemUseCase.execute(
                userId,
                restaurantId,
                name,
                description,
                price,
                availableForInStoreOnly != null ? availableForInStoreOnly : false,
                fileUploadRequest
            );

            var response = menuItemMapper.toMenuItemResponse(createdMenuItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(UUID userId, UUID restaurantId, UUID itemId) {


        deleteMenuItemUseCase.execute(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MenuItemResponse> getMenuItem(UUID userId, UUID restaurantId, UUID itemId) {


        var menuItemOptional = findMenuItemByIdUseCase.execute(itemId);

        if (menuItemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var menuItem = menuItemOptional.get();

        if (!validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var response = menuItemMapper.toMenuItemResponse(menuItem);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(UUID userId, UUID restaurantId) {

        var menuItems = findAllMenuItemsUseCase.execute(restaurantId);
        var response = menuItemMapper.toMenuItemResponseList(menuItems);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            UUID userId,
            UUID restaurantId,
            UUID itemId,
            String name,
            String description,
            Double price,
            Boolean availableForInStoreOnly,
            MultipartFile photo) {


        var existingMenuItemOptional = findMenuItemByIdUseCase.execute(itemId);
        if (existingMenuItemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            FileUploadRequest fileUploadRequest = null;
            if (photo != null && !photo.isEmpty()) {
                fileUploadRequest = new FileUploadRequest(
                    photo.getInputStream(),
                    photo.getSize(),
                    photo.getContentType(),
                    photo.getOriginalFilename()
                );
            }

            var updatedMenuItem = updateMenuItemUseCase.execute(
                userId,
                itemId,
                name,
                description,
                price,
                availableForInStoreOnly != null ? availableForInStoreOnly : false,
                fileUploadRequest
            );

            var response = menuItemMapper.toMenuItemResponse(updatedMenuItem);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
