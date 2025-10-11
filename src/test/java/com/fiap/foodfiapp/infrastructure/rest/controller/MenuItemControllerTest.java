package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.infrastructure.rest.mapper.MenuItemMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.MenuItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    @Mock
    private CreateMenuItemUseCase createMenuItemUseCase;

    @Mock
    private FindMenuItemByIdUseCase findMenuItemByIdUseCase;

    @Mock
    private UpdateMenuItemUseCase updateMenuItemUseCase;

    @Mock
    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    @Mock
    private FindAllMenuItemsUseCase findAllMenuItemsUseCase;

    @Mock
    private ValidateMenuItemOwnershipUseCase validateMenuItemOwnershipUseCase;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private MenuItemMapper menuItemMapper;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private MenuItemController menuItemController;

    private UUID userId;
    private UUID restaurantId;
    private UUID itemId;
    private MenuItem menuItem;
    private MenuItemResponse menuItemResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        itemId = UUID.randomUUID();

        menuItem = new MenuItem(
            itemId,
            "Test Burger",
            "Delicious test burger",
            25.99,
            false,
            "http://example.com/photo.jpg",
            restaurantId,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );

        menuItemResponse = new MenuItemResponse();
        menuItemResponse.setId(itemId);
        menuItemResponse.setName("Test Burger");
        menuItemResponse.setDescription("Delicious test burger");
        menuItemResponse.setPrice(25.99);
        menuItemResponse.setAvailableForInStoreOnly(false);
        menuItemResponse.setPhotoUrl("http://example.com/photo.jpg");
    }

    // ========== CREATE MENU ITEM TESTS ==========

    @Test
    void shouldCreateMenuItemSuccessfully() throws IOException {
        // Arrange
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));
        when(multipartFile.getSize()).thenReturn(4L);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");

        when(createMenuItemUseCase.execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), any(FileUploadRequest.class)))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
            userId, restaurantId, "Test Burger", "Delicious burger", 25.99, false, multipartFile);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(itemId, response.getBody().getId());
        assertEquals("Test Burger", response.getBody().getName());

        verify(createMenuItemUseCase).execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), any(FileUploadRequest.class));
        verify(menuItemMapper).toMenuItemResponse(menuItem);
    }

    @Test
    void shouldCreateMenuItemWithoutPhoto() throws IOException {
        // Arrange
        when(createMenuItemUseCase.execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), isNull()))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
            userId, restaurantId, "Test Burger", "Delicious burger", 25.99, false, null);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createMenuItemUseCase).execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), isNull());
    }

    @Test
    void shouldCreateMenuItemWithEmptyPhoto() throws IOException {
        // Arrange
        when(multipartFile.isEmpty()).thenReturn(true);
        when(createMenuItemUseCase.execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), isNull()))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
            userId, restaurantId, "Test Burger", "Delicious burger", 25.99, false, multipartFile);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createMenuItemUseCase).execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), isNull());
    }

    @Test
    void shouldCreateMenuItemWithNullAvailableForInStoreOnly() throws IOException {
        // Arrange
        when(createMenuItemUseCase.execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), isNull()))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
            userId, restaurantId, "Test Burger", "Delicious burger", 25.99, null, null);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createMenuItemUseCase).execute(eq(userId), eq(restaurantId), eq("Test Burger"),
            eq("Delicious burger"), eq(25.99), eq(false), isNull());
    }

    @Test
    void shouldReturnInternalServerErrorWhenCreateMenuItemThrowsIOException() throws IOException {
        // Arrange
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenThrow(new IOException("File error"));

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
            userId, restaurantId, "Test Burger", "Delicious burger", 25.99, false, multipartFile);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(createMenuItemUseCase, never()).execute(any(), any(), any(), any(), any(), any(), any());
    }

    // ========== DELETE MENU ITEM TESTS ==========

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        // Arrange
        doNothing().when(deleteMenuItemUseCase).execute(userId, itemId);

        // Act
        ResponseEntity<Void> response = menuItemController.deleteMenuItem(userId, restaurantId, itemId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteMenuItemUseCase).execute(userId, itemId);
    }

    // ========== GET MENU ITEM TESTS ==========

    @Test
    void shouldGetMenuItemSuccessfully() {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(true);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.getMenuItem(userId, restaurantId, itemId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(itemId, response.getBody().getId());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase).execute(itemId, restaurantId);
        verify(menuItemMapper).toMenuItemResponse(menuItem);
    }

    @Test
    void shouldReturnNotFoundWhenMenuItemDoesNotExist() {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.getMenuItem(userId, restaurantId, itemId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase, never()).execute(any(), any());
        verify(menuItemMapper, never()).toMenuItemResponse(any());
    }

    @Test
    void shouldReturnForbiddenWhenUserDoesNotOwnMenuItem() {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(false);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.getMenuItem(userId, restaurantId, itemId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase).execute(itemId, restaurantId);
        verify(menuItemMapper, never()).toMenuItemResponse(any());
    }

    // ========== LIST MENU ITEMS TESTS ==========

    @Test
    void shouldListMenuItemsSuccessfully() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        List<MenuItemResponse> menuItemResponses = Arrays.asList(menuItemResponse);

        when(findAllMenuItemsUseCase.execute(restaurantId)).thenReturn(menuItems);
        when(menuItemMapper.toMenuItemResponseList(menuItems)).thenReturn(menuItemResponses);

        // Act
        ResponseEntity<List<MenuItemResponse>> response = menuItemController.listMenuItems(userId, restaurantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(itemId, response.getBody().get(0).getId());

        verify(findAllMenuItemsUseCase).execute(restaurantId);
        verify(menuItemMapper).toMenuItemResponseList(menuItems);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItemsFound() {
        // Arrange
        when(findAllMenuItemsUseCase.execute(restaurantId)).thenReturn(Collections.emptyList());
        when(menuItemMapper.toMenuItemResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<MenuItemResponse>> response = menuItemController.listMenuItems(userId, restaurantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(findAllMenuItemsUseCase).execute(restaurantId);
        verify(menuItemMapper).toMenuItemResponseList(Collections.emptyList());
    }

    @Test
    void shouldListMultipleMenuItems() {
        // Arrange
        UUID secondItemId = UUID.randomUUID();
        MenuItem secondMenuItem = new MenuItem(
            secondItemId, "Pizza", "Delicious pizza", 35.99, true,
            "http://example.com/pizza.jpg", restaurantId, OffsetDateTime.now(), OffsetDateTime.now());

        MenuItemResponse secondResponse = new MenuItemResponse();
        secondResponse.setId(secondItemId);
        secondResponse.setName("Pizza");

        List<MenuItem> menuItems = Arrays.asList(menuItem, secondMenuItem);
        List<MenuItemResponse> responses = Arrays.asList(menuItemResponse, secondResponse);

        when(findAllMenuItemsUseCase.execute(restaurantId)).thenReturn(menuItems);
        when(menuItemMapper.toMenuItemResponseList(menuItems)).thenReturn(responses);

        // Act
        ResponseEntity<List<MenuItemResponse>> response = menuItemController.listMenuItems(userId, restaurantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Test Burger", response.getBody().get(0).getName());
        assertEquals("Pizza", response.getBody().get(1).getName());
    }

    // ========== UPDATE MENU ITEM TESTS ==========

    @Test
    void shouldUpdateMenuItemSuccessfully() throws IOException {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(true);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));
        when(multipartFile.getSize()).thenReturn(4L);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");

        MenuItem updatedMenuItem = new MenuItem(
            itemId, "Updated Burger", "Updated description", 29.99, true,
            "http://example.com/new-photo.jpg", restaurantId, OffsetDateTime.now(), OffsetDateTime.now());

        MenuItemResponse updatedResponse = new MenuItemResponse();
        updatedResponse.setId(itemId);
        updatedResponse.setName("Updated Burger");

        when(updateMenuItemUseCase.execute(eq(userId), eq(itemId), eq("Updated Burger"),
            eq("Updated description"), eq(29.99), eq(true), any(FileUploadRequest.class)))
            .thenReturn(updatedMenuItem);
        when(menuItemMapper.toMenuItemResponse(updatedMenuItem)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
            userId, restaurantId, itemId, "Updated Burger", "Updated description", 29.99, true, multipartFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Burger", response.getBody().getName());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase).execute(itemId, restaurantId);
        verify(updateMenuItemUseCase).execute(eq(userId), eq(itemId), eq("Updated Burger"),
            eq("Updated description"), eq(29.99), eq(true), any(FileUploadRequest.class));
        verify(menuItemMapper).toMenuItemResponse(updatedMenuItem);
    }

    @Test
    void shouldUpdateMenuItemWithoutPhoto() throws IOException {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(true);

        when(updateMenuItemUseCase.execute(eq(userId), eq(itemId), eq("Updated Burger"),
            eq("Updated description"), eq(29.99), eq(false), isNull()))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
            userId, restaurantId, itemId, "Updated Burger", "Updated description", 29.99, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updateMenuItemUseCase).execute(eq(userId), eq(itemId), eq("Updated Burger"),
            eq("Updated description"), eq(29.99), eq(false), isNull());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentMenuItem() throws IOException {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
            userId, restaurantId, itemId, "Updated Burger", "Updated description", 29.99, true, null);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase, never()).execute(any(), any());
        verify(updateMenuItemUseCase, never()).execute(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void shouldReturnForbiddenWhenUpdatingMenuItemWithoutOwnership() throws IOException {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(false);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
            userId, restaurantId, itemId, "Updated Burger", "Updated description", 29.99, true, null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase).execute(itemId, restaurantId);
        verify(updateMenuItemUseCase, never()).execute(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUpdateMenuItemThrowsIOException() throws IOException {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(true);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenThrow(new IOException("File error"));

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
            userId, restaurantId, itemId, "Updated Burger", "Updated description", 29.99, true, multipartFile);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(findMenuItemByIdUseCase).execute(itemId);
        verify(validateMenuItemOwnershipUseCase).execute(itemId, restaurantId);
        verify(updateMenuItemUseCase, never()).execute(any(), any(), any(), any(), any(), any(), any());
    }

    // ========== EDGE CASES AND ADDITIONAL TESTS ==========

    @Test
    void shouldHandleNullParametersInCreateMenuItem() throws IOException {
        // Arrange
        when(createMenuItemUseCase.execute(eq(userId), eq(restaurantId), isNull(),
            isNull(), isNull(), eq(false), isNull()))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.createMenuItem(
            userId, restaurantId, null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createMenuItemUseCase).execute(eq(userId), eq(restaurantId), isNull(),
            isNull(), isNull(), eq(false), isNull());
    }

    @Test
    void shouldHandleNullParametersInUpdateMenuItem() throws IOException {
        // Arrange
        when(findMenuItemByIdUseCase.execute(itemId)).thenReturn(Optional.of(menuItem));
        when(validateMenuItemOwnershipUseCase.execute(itemId, restaurantId)).thenReturn(true);
        when(updateMenuItemUseCase.execute(eq(userId), eq(itemId), isNull(),
            isNull(), isNull(), eq(false), isNull()))
            .thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(menuItem)).thenReturn(menuItemResponse);

        // Act
        ResponseEntity<MenuItemResponse> response = menuItemController.updateMenuItem(
            userId, restaurantId, itemId, null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updateMenuItemUseCase).execute(eq(userId), eq(itemId), isNull(),
            isNull(), isNull(), eq(false), isNull());
    }

    @Test
    void shouldVerifyAllUseCasesAreCalledWithCorrectParameters() throws IOException {
        // Test that all use cases are called with the expected parameters

        // Create
        when(createMenuItemUseCase.execute(any(), any(), any(), any(), any(), any(), any())).thenReturn(menuItem);
        when(menuItemMapper.toMenuItemResponse(any())).thenReturn(menuItemResponse);

        menuItemController.createMenuItem(userId, restaurantId, "Test", "Desc", 10.0, true, null);
        verify(createMenuItemUseCase).execute(eq(userId), eq(restaurantId), eq("Test"),
            eq("Desc"), eq(10.0), eq(true), isNull());

        // List
        when(findAllMenuItemsUseCase.execute(any())).thenReturn(Arrays.asList(menuItem));
        when(menuItemMapper.toMenuItemResponseList(any())).thenReturn(Arrays.asList(menuItemResponse));

        menuItemController.listMenuItems(userId, restaurantId);
        verify(findAllMenuItemsUseCase).execute(eq(restaurantId));

        // Delete
        doNothing().when(deleteMenuItemUseCase).execute(any(), any());

        menuItemController.deleteMenuItem(userId, restaurantId, itemId);
        verify(deleteMenuItemUseCase).execute(eq(userId), eq(itemId));
    }
}
