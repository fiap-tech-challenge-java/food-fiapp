package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.DeleteRestaurantUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private DeleteRestaurantUseCaseImpl deleteRestaurantUseCase;

    private UUID restaurantId;
    private UUID authenticatedUserId;
    private Restaurant testRestaurant;
    private List<MenuItem> testMenuItems;
    private List<Addresses> testAddresses;

    @BeforeEach
    void setUp() {
        // Use a fixed authenticated user ID for all tests
        authenticatedUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        
        restaurantId = UUID.randomUUID();
        testRestaurant = new Restaurant(
            restaurantId,
            "Test Restaurant",
            "Test Cuisine",
            "09:00-22:00",
            authenticatedUserId, // Ensure the restaurant is owned by the authenticated user
            "Test Description",
            null
        );

        // Setup test menu items
        MenuItem item1 = new MenuItem(
            UUID.randomUUID(),
            "Item 1",
            "Description 1",
            10.0,
            false,
            "http://example.com/item1.jpg",
            restaurantId,
            java.time.OffsetDateTime.now(),
            java.time.OffsetDateTime.now()
        );
        
        MenuItem item2 = new MenuItem(
            UUID.randomUUID(),
            "Item 2",
            "Description 2",
            15.0,
            false,
            "http://example.com/item2.jpg",
            restaurantId,
            java.time.OffsetDateTime.now(),
            java.time.OffsetDateTime.now()
        );
        
        testMenuItems = Arrays.asList(item1, item2);

        // Setup test addresses
        Addresses address1 = new Addresses(
            UUID.randomUUID(),
            "123 Main St",
            "123",
            "Apt 4B",
            "Downtown",
            "City",
            "CA",
            "12345"
        );
        
        Addresses address2 = new Addresses(
            UUID.randomUUID(),
            "456 Oak St",
            "456",
            "",
            "Uptown",
            "City",
            "CA",
            "54321"
        );
        
        // Set owner ID if needed through setter or repository
        
        testAddresses = Arrays.asList(address1, address2);
    }

    @Test
    void shouldDeleteRestaurantAndAssociatedEntities() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(testMenuItems);
        when(addressRepository.findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription()))).thenReturn(testAddresses);
        
        // Mock the menu item save operations
        for (MenuItem item : testMenuItems) {
            when(menuItemRepository.save(item)).thenReturn(item);
        }
        
        // Act
        deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId);
        
        // Assert - Verify restaurant is deleted
        verify(restaurantRepository).delete(restaurantId);
        
        // Verify menu items are deactivated (saved with isActive = false)
        verify(menuItemRepository, times(testMenuItems.size())).save(any(MenuItem.class));
        for (MenuItem item : testMenuItems) {
            assertFalse(item.getIsActive(), "Menu item should be deactivated");
        }
        
        // Verify addresses are deactivated (saved with isActive = false)
        verify(addressRepository, times(testAddresses.size())).save(any(Addresses.class), eq(restaurantId), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription()));
        for (Addresses address : testAddresses) {
            assertFalse(address.getIsActive(), "Address should be deactivated");
        }
    }

    @Test
    void shouldHandleNonExistentRestaurant() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(null);
        
        // Act & Assert - Should complete without errors (idempotent operation)
        assertDoesNotThrow(
            () -> deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId)
        );
        
        // Verify the repository was called once to check for the restaurant
        verify(restaurantRepository).findById(restaurantId);
        // Verify no other interactions with repositories for non-existent restaurant
        verifyNoMoreInteractions(restaurantRepository);
        verifyNoInteractions(menuItemRepository);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldHandleNoMenuItems() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription())))
            .thenReturn(Collections.emptyList());
        
        // Act
        assertDoesNotThrow(() -> deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId));
        
        // Assert - Verify restaurant is deleted
        verify(restaurantRepository).delete(restaurantId);
        
        // Verify no menu items to deactivate
        verify(menuItemRepository, never()).save(any(MenuItem.class));
        
        // Verify no addresses to deactivate
        verify(addressRepository, never()).save(any(Addresses.class), any(UUID.class), anyString());
    }

    @Test
    void shouldHandleNullRestaurantId() {
        // Act & Assert - Should not throw any exception for null ID (idempotent operation)
        assertDoesNotThrow(
            () -> deleteRestaurantUseCase.execute(authenticatedUserId, null)
        );
        
        // Verify no interactions with repositories for null ID
        verifyNoInteractions(restaurantRepository);
        verifyNoInteractions(menuItemRepository);
        verifyNoInteractions(addressRepository);
        
        // Reset mocks to clear any interactions from setup
        reset(restaurantRepository, menuItemRepository, addressRepository);
        
        // Verify no interactions after reset
        verifyNoInteractions(restaurantRepository);
        verifyNoInteractions(menuItemRepository);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldHandleMenuItemSaveFailure() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(testMenuItems);
        
        // First menu item save succeeds, second fails
        when(menuItemRepository.save(testMenuItems.get(0))).thenReturn(testMenuItems.get(0));
        when(menuItemRepository.save(testMenuItems.get(1))).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert - Should throw exception when menu item save fails
        Exception exception = assertThrows(
            RuntimeException.class,
            () -> deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId)
        );
        
        // Verify the exception is propagated
        assertEquals("Database error", exception.getMessage());
        
        // Verify restaurant is still deleted
        verify(restaurantRepository).delete(restaurantId);
        
        // Verify all menu items were attempted to be saved
        verify(menuItemRepository, times(testMenuItems.size())).save(any(MenuItem.class));
        
        // Verify no address processing occurred due to the menu item save failure
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldHandleAddressSaveFailure() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(testMenuItems);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem item = invocation.getArgument(0);
            item.setIsActive(false);
            return item;
        });
        when(addressRepository.findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription())))
            .thenReturn(testAddresses);
        
        // First address save succeeds, second fails
        when(addressRepository.save(eq(testAddresses.get(0)), eq(restaurantId), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription())))
            .thenReturn(testAddresses.get(0));
        when(addressRepository.save(eq(testAddresses.get(1)), eq(restaurantId), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription())))
            .thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert - Should still try to process all addresses even if some fail
        Exception exception = assertThrows(
            RuntimeException.class,
            () -> deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId)
        );
        
        // Verify the exception is propagated
        assertEquals("Database error", exception.getMessage());
        
        // Verify restaurant is still deleted
        verify(restaurantRepository).delete(restaurantId);
        
        // Verify all menu items were deactivated
        verify(menuItemRepository, times(testMenuItems.size())).save(any(MenuItem.class));
        
        // Verify all addresses were attempted to be saved
        verify(addressRepository, times(testAddresses.size()))
            .save(any(Addresses.class), eq(restaurantId), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription()));
    }

    @Test
    void shouldHandleMultipleCalls() {
        // Arrange - Create a second restaurant with the same owner
        UUID secondRestaurantId = UUID.randomUUID();
        Restaurant secondRestaurant = new Restaurant(
            secondRestaurantId,
            "Second Restaurant",
            "Test Cuisine",
            "09:00-22:00",
            authenticatedUserId, // Same owner as the first restaurant
            "Test Description",
            null
        );
        
        // Mock repository responses for first restaurant
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(testMenuItems);
        when(addressRepository.findByOwner(restaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription()))
            .thenReturn(testAddresses);
        
        // Mock repository responses for second restaurant
        when(restaurantRepository.findById(secondRestaurantId)).thenReturn(secondRestaurant);
        when(menuItemRepository.findAllByRestaurantId(secondRestaurantId)).thenReturn(Collections.emptyList());
        when(addressRepository.findByOwner(secondRestaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription()))
            .thenReturn(Collections.emptyList());
        
        // Mock successful saves
        for (MenuItem item : testMenuItems) {
            when(menuItemRepository.save(item)).thenReturn(item);
        }
        for (Addresses address : testAddresses) {
            when(addressRepository.save(eq(address), eq(restaurantId), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription())))
                .thenReturn(address);
        }
        
        // Act - Perform multiple delete operations
        deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId);
        deleteRestaurantUseCase.execute(authenticatedUserId, secondRestaurantId);
        
        // Assert - Verify both restaurants were processed
        verify(restaurantRepository).delete(restaurantId);
        verify(restaurantRepository).delete(secondRestaurantId);
        
        // Verify menu items and addresses were processed for the first restaurant
        verify(menuItemRepository, times(testMenuItems.size())).save(any(MenuItem.class));
        verify(addressRepository, times(testAddresses.size()))
            .save(any(Addresses.class), eq(restaurantId), eq(AddressOwnerTypeEnum.RESTAURANT.getDescription()));
        
        // Verify no menu items or addresses for the second restaurant (empty lists)
        verify(menuItemRepository, never()).save(argThat(item -> item.getRestaurantId().equals(secondRestaurantId)));
        verify(addressRepository, never()).save(any(Addresses.class), eq(secondRestaurantId), anyString());
    }

    @Test
    void shouldHandleRestaurantWithNoAddresses() {
        // Arrange
        when(restaurantRepository.findById(restaurantId)).thenReturn(testRestaurant);
        when(menuItemRepository.findAllByRestaurantId(restaurantId)).thenReturn(testMenuItems);
        when(addressRepository.findByOwner(restaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription()))
            .thenReturn(Collections.emptyList());
        
        // Act
        deleteRestaurantUseCase.execute(authenticatedUserId, restaurantId);
        
        // Assert
        verify(restaurantRepository).delete(restaurantId);
        verify(menuItemRepository, times(testMenuItems.size())).save(any(MenuItem.class));
        verify(addressRepository, never()).save(any(Addresses.class), any(UUID.class), anyString());
    }
}
