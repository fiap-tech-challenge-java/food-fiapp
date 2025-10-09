package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.CreateMenuItemUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.ValidateRestaurantOwnershipUseCase;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @Mock
    private ValidateRestaurantOwnershipUseCase validateRestaurantOwnershipUseCase;

    @Captor
    private ArgumentCaptor<MenuItem> menuItemCaptor;

    private CreateMenuItemUseCaseImpl createMenuItemUseCase;

    private UUID restaurantId;
    private String name;
    private String description;
    private Double price;
    private Boolean availableForInStoreOnly;
    private FileUploadRequest photo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createMenuItemUseCase = new CreateMenuItemUseCaseImpl(
            menuItemRepository, 
            restaurantRepository, 
            fileStorageRepository,
            validateRestaurantOwnershipUseCase
        );
        
        // Mock the ownership validation to return true by default
        when(validateRestaurantOwnershipUseCase.execute(any(UUID.class), any(UUID.class))).thenReturn(true);

        restaurantId = UUID.randomUUID();
        name = "Test Burger";
        description = "Delicious test burger";
        price = 29.99;
        availableForInStoreOnly = false;
        
        // Create a mock photo
        byte[] photoContent = "test photo content".getBytes();
        photo = new FileUploadRequest(
            new ByteArrayInputStream(photoContent),
            photoContent.length,
            "image/jpeg",
            "test.jpg"
        );
    }

    @Test
    void shouldCreateMenuItemSuccessfully() throws IOException {
        // Arrange
        String photoUrl = "https://example.com/photos/test.jpg";
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString())).thenReturn(photoUrl);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        // Act
        UUID userId = UUID.randomUUID();
        MenuItem result = createMenuItemUseCase.execute(
            userId, restaurantId, name, description, price, availableForInStoreOnly, photo);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(price, result.getPrice());
        assertEquals(availableForInStoreOnly, result.isLocalOnly());
        assertEquals(restaurantId, result.getRestaurantId());
        assertEquals(photoUrl, result.getPhotoUrl());
        
        verify(restaurantRepository).existsById(restaurantId);
        verify(fileStorageRepository).store(any(), anyLong(), eq("image/jpeg"), anyString());
        verify(menuItemRepository).save(menuItemCaptor.capture());
        
        MenuItem capturedMenuItem = menuItemCaptor.getValue();
        assertNotNull(capturedMenuItem.getId()); // ID is set by repository
        assertEquals(name, capturedMenuItem.getName());
    }

    @Test
    void shouldCreateMenuItemWithoutPhoto() throws IOException {
        // Arrange
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        // Act
        UUID userId = UUID.randomUUID();
        MenuItem result = createMenuItemUseCase.execute(
            userId, restaurantId, name, description, price, availableForInStoreOnly, null);

        // Assert
        assertNotNull(result);
        assertNull(result.getPhotoUrl());
        verify(fileStorageRepository, never()).store(any(), anyLong(), anyString(), anyString());
    }

    @Test
    void shouldSetAvailableForInStoreOnlyToTrueWhenNull() throws IOException {
        // Arrange
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        // Act
        UUID userId = UUID.randomUUID();
        MenuItem result = createMenuItemUseCase.execute(
            userId, restaurantId, name, description, price, null, null);

        // Assert
        assertFalse(result.isLocalOnly());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        // Act & Assert
        UUID userId = UUID.randomUUID();
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createMenuItemUseCase.execute(userId, restaurantId, name, description, price, availableForInStoreOnly, photo)
        );
        
        assertEquals("Restaurant not found or is inactive.", exception.getMessage());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void shouldHandleFileStorageError() throws IOException {
        // Arrange
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString()))
            .thenThrow(new IOException("Failed to store file"));

        // Act & Assert
        UUID userId = UUID.randomUUID();
        assertThrows(
            IOException.class,
            () -> createMenuItemUseCase.execute(userId, restaurantId, name, description, price, availableForInStoreOnly, photo)
        );
        
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void shouldGenerateUniqueFilenameForPhoto() throws IOException {
        // Arrange
        String photoUrl = "https://example.com/photos/unique.jpg";
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(fileStorageRepository.store(any(), anyLong(), anyString(), anyString())).thenReturn(photoUrl);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        // Act
        UUID userId = UUID.randomUUID();
        createMenuItemUseCase.execute(userId, restaurantId, name, description, price, availableForInStoreOnly, photo);

        // Assert
        verify(fileStorageRepository).store(
            any(),
            anyLong(),
            eq("image/jpeg"),
            argThat(filename -> 
                filename.startsWith(restaurantId.toString() + "-") && 
                filename.endsWith(".jpg")
            )
        );
    }
}
