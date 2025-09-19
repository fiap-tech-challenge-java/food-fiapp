package com.fiap.foodfiapp.infrastructure.rest;

import com.fiap.foodfiapp.api.MenuItemsApi;
import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.model.MenuItemRequest;
import com.fiap.foodfiapp.model.MenuItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MenuItemController implements MenuItemsApi {
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetAllMenuItemsUseCase getAllMenuItemsUseCase;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    @Override
    public ResponseEntity<List<MenuItemResponse>> restaurantsRestaurantIdMenuItemsGet(UUID restaurantId) {
        List<MenuItemResponse> items = getAllMenuItemsUseCase.execute(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(items);
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> restaurantsRestaurantIdMenuItemsPost(UUID restaurantId, MenuItemRequest request) {
        MenuItem menuItem = new MenuItem(
                null,
                request.getName(),
                request.getDescription(),
                BigDecimal.valueOf(request.getPrice()),
                request.getAvailableForInStoreOnly(),
                request.getPhotoBase64(),
                restaurantId,
                null,
                null
        );

        MenuItem createdItem = createMenuItemUseCase.execute(menuItem);
        return ResponseEntity.ok(toResponse(createdItem));
    }

    @Override
    public ResponseEntity<MenuItemResponse> restaurantsRestaurantIdMenuItemsItemIdGet(UUID restaurantId, UUID itemId) {
        return getMenuItemByIdUseCase.execute(itemId)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<MenuItemResponse> restaurantsRestaurantIdMenuItemsItemIdPut(UUID restaurantId, UUID itemId, MenuItemRequest request) {
        MenuItem menuItemUpdate = new MenuItem(
                itemId,
                request.getName(),
                request.getDescription(),
                BigDecimal.valueOf(request.getPrice()),
                request.getAvailableForInStoreOnly(),
                request.getPhotoBase64(),
                restaurantId,
                null,
                null
        );

        MenuItem updatedItem = updateMenuItemUseCase.execute(itemId, menuItemUpdate);
        return ResponseEntity.ok(toResponse(updatedItem));
    }

    @Override
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Void> restaurantsRestaurantIdMenuItemsItemIdDelete(UUID restaurantId, UUID itemId) {
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
        response.setPhotoBase64(menuItem.photoBase64());
        return response;
    }
}
