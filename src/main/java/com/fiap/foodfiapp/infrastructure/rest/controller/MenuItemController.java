package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.MenuItemsApi;
import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
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

    private final MenuItemMapper menuItemMapper = MenuItemMapper.INSTANCE;

    @Override
    public ResponseEntity<MenuItemResponse> createMenuItem(
            UUID userId,
            UUID restaurantId,
            String name,
            String description,
            Double price,
            Boolean availableForInStoreOnly,
            MultipartFile photo) {

        // Main authentication check
     //   if (!authenticationService.canAccessUserProfile(userId)) {
     //       throw new UnauthorizedException("Permissão negada. Operação não autorizada para este usuário.");
     //   }

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
        // Main authentication check
    //    if (!authenticationService.canAccessUserProfile(userId)) {
    //        throw new UnauthorizedException("Permissão negada. Operação não autorizada para este usuário.");
    //    }

        deleteMenuItemUseCase.execute(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MenuItemResponse> getMenuItem(UUID userId, UUID restaurantId, UUID itemId) {
        // Main authentication check
     //   if (!authenticationService.canAccessUserProfile(userId)) {
     //       throw new UnauthorizedException("Permissão negada. Operação não autorizada para este usuário.");
     //   }

        var menuItemOptional = findMenuItemByIdUseCase.execute(itemId);

        if (menuItemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var menuItem = menuItemOptional.get();

        // Use validation use case instead of directly accessing domain entity
        if (!validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var response = menuItemMapper.toMenuItemResponse(menuItem);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(UUID userId, UUID restaurantId) {
        // Main authentication check
    //    if (!authenticationService.canAccessUserProfile(userId)) {
    //        throw new UnauthorizedException("Permissão negada. Operação não autorizada para este usuário.");
     //   }

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

        // Main authentication check
    //    if (!authenticationService.canAccessUserProfile(userId)) {
    //        throw new UnauthorizedException("Permissão negada. Operação não autorizada para este usuário.");
    //    }

        // Find existing menu item and validate ownership using use case
        var existingMenuItemOptional = findMenuItemByIdUseCase.execute(itemId);
        if (existingMenuItemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Use validation use case instead of directly accessing domain entity
        if (!validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
