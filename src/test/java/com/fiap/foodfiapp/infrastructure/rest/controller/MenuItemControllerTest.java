package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.FileStorageException;
import com.fiap.foodfiapp.model.MenuItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    @Mock
    private CreateMenuItemUseCase createMenuItemUseCase;

    @Mock
    private GetMenuItemsByRestaurantUseCase getMenuItemsByRestaurantUseCase;

    @Mock
    private GetMenuItemByIdUseCase getMenuItemByIdUseCase;

    @Mock
    private UpdateMenuItemUseCase updateMenuItemUseCase;

    @Mock
    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    @InjectMocks
    private MenuItemController menuItemController;

    private UUID restaurantId;
    private UUID itemId;
    private MenuItem menuItem;
    private MultipartFile photo;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        menuItem = new MenuItem(
                itemId,
                "Test Item",
                "Test Description",
                BigDecimal.valueOf(25.99),
                false,
                "photo-url.jpg",
                restaurantId,
                null,
                null
        );
        photo = new MockMultipartFile("photo", "test.jpg", "image/jpeg", "test image".getBytes());
    }

    @Test
    void shouldReturnMenuItemsListSuccessfully() {
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(getMenuItemsByRestaurantUseCase.execute(restaurantId)).thenReturn(menuItems);

        ResponseEntity<List<MenuItemResponse>> response = menuItemController.listMenuItems(restaurantId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(itemId);
        verify(getMenuItemsByRestaurantUseCase).execute(restaurantId);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItems() {
        when(getMenuItemsByRestaurantUseCase.execute(restaurantId)).thenReturn(Arrays.asList());

        ResponseEntity<List<MenuItemResponse>> response = menuItemController.listMenuItems(restaurantId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void shouldCreateMenuItemSuccessfully() throws IOException {
        when(createMenuItemUseCase.execute(any(MenuItem.class), eq(photo))).thenReturn(menuItem);

        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
                restaurantId, "Test Item", "Test Description", 25.99, false, photo);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Test Item");
        verify(createMenuItemUseCase).execute(any(MenuItem.class), eq(photo));
    }

    @Test
    void shouldCreateMenuItemWithDefaultAvailability() throws IOException {
        when(createMenuItemUseCase.execute(any(MenuItem.class), eq(photo))).thenReturn(menuItem);

        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
                restaurantId, "Test Item", "Test Description", 25.99, null, photo);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(createMenuItemUseCase).execute(any(MenuItem.class), eq(photo));
    }

    @Test
    void shouldThrowFileStorageExceptionWhenCreateFails() throws IOException {
        when(createMenuItemUseCase.execute(any(MenuItem.class), eq(photo)))
                .thenThrow(new IOException("File upload failed"));

        assertThatThrownBy(() -> menuItemController.createMenuItem(
                restaurantId, "Test Item", "Test Description", 25.99, false, photo))
                .isInstanceOf(FileStorageException.class)
                .hasMessageContaining("Failed to store menu item photo");
    }

    @Test
    void shouldGetMenuItemSuccessfully() {
        when(getMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));

        ResponseEntity<MenuItemResponse> response = menuItemController.getMenuItem(restaurantId, itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(itemId);
        verify(getMenuItemByIdUseCase).execute(itemId);
    }

    @Test
    void shouldReturnNotFoundWhenMenuItemDoesNotExist() {
        when(getMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.empty());

        ResponseEntity<MenuItemResponse> response = menuItemController.getMenuItem(restaurantId, itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() throws IOException {
        MenuItem updatedMenuItem = new MenuItem(
                itemId, "Updated Item", "Updated Description",
                BigDecimal.valueOf(29.99), true, "new-photo.jpg",
                restaurantId, null, null
        );
        when(updateMenuItemUseCase.execute(eq(itemId), any(MenuItem.class), eq(photo)))
                .thenReturn(updatedMenuItem);

        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
                restaurantId, itemId, "Updated Item", "Updated Description", 29.99, true, photo);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated Item");
        verify(updateMenuItemUseCase).execute(eq(itemId), any(MenuItem.class), eq(photo));
    }

    @Test
    void shouldUpdateMenuItemWithDefaultAvailability() throws IOException {
        when(updateMenuItemUseCase.execute(eq(itemId), any(MenuItem.class), eq(photo)))
                .thenReturn(menuItem);

        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
                restaurantId, itemId, "Updated Item", "Updated Description", 29.99, null, photo);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(updateMenuItemUseCase).execute(eq(itemId), any(MenuItem.class), eq(photo));
    }

    @Test
    void shouldThrowFileStorageExceptionWhenUpdateFails() throws IOException {
        when(updateMenuItemUseCase.execute(eq(itemId), any(MenuItem.class), eq(photo)))
                .thenThrow(new IOException("File upload failed"));

        assertThatThrownBy(() -> menuItemController.updateMenuItem(
                restaurantId, itemId, "Updated Item", "Updated Description", 29.99, false, photo))
                .isInstanceOf(FileStorageException.class)
                .hasMessageContaining("Failed to store menu item photo");
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        doNothing().when(deleteMenuItemUseCase).execute(itemId);

        ResponseEntity<Void> response = menuItemController.deleteMenuItem(restaurantId, itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(deleteMenuItemUseCase).execute(itemId);
    }
}
