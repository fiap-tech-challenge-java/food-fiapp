package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.UpdateRestaurantUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    private UpdateRestaurantUseCaseImpl updateRestaurantUseCase;

    private UUID restaurantId;
    private Restaurant existingRestaurant;
    private Restaurant updateData;

    @BeforeEach
    void setUp() {
        // Initialize the use case with all required dependencies
        updateRestaurantUseCase = new UpdateRestaurantUseCaseImpl(restaurantRepository, addressRepository, userRepository);
        
        restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        
        // Setup existing restaurant
        existingRestaurant = new Restaurant(
            restaurantId,
            "Original Name",
            "ITALIAN",
            "09:00-22:00",
            ownerId,
            "Original Description",
            null
        );
        
        // Setup update data with minimal required fields
        updateData = new Restaurant(
            null, // ID will be set by the use case
            null, // Name will be set in tests
            null, // Cuisine type will be set in tests
            null, // Opening hours will be set in tests
            null, // Owner ID will be set by the use case
            null, // Description will be set in tests
            null  // Address will be set in tests if needed
        );
    }

    @Test
    void shouldUpdateRestaurantName() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("Updated Name");
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("ITALIAN", result.getCuisineType()); // Should remain unchanged
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldUpdateCuisineType() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setCuisineType("JAPANESE");
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertEquals("JAPANESE", result.getCuisineType());
        assertEquals("Original Name", result.getName()); // Should remain unchanged
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldUpdateOpeningHours() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setOpeningHours("10:00-23:00");
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertEquals("10:00-23:00", result.getOpeningHours());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldUpdateMultipleFields() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("New Name");
        updateData.setCuisineType("MEXICAN");
        updateData.setOpeningHours("11:00-21:00");
        updateData.setDescription("Updated description");
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("MEXICAN", result.getCuisineType());
        assertEquals("11:00-21:00", result.getOpeningHours());
        assertEquals("Updated description", result.getDescription());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Arrange
        UUID authenticatedUserId = UUID.randomUUID();
        when(restaurantRepository.findById(restaurantId)).thenReturn(null);
        
        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData)
        );
        
        assertEquals("Restaurant not found.", exception.getMessage());
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldNotAllowUpdateForUnauthorizedUser() {
        // Arrange
        UUID unauthorizedUserId = UUID.randomUUID(); // Different from the owner ID
        updateData.setName("Updated Name");
        
        // Mock user as non-owner
        User unauthorizedUser = new User();
        unauthorizedUser.setId(unauthorizedUserId);
        UserType userType = new UserType();
        userType.setName("CUSTOMER");
        unauthorizedUser.setUserType(userType);
        when(userRepository.findById(unauthorizedUserId)).thenReturn(Optional.of(unauthorizedUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        
        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateRestaurantUseCase.execute(unauthorizedUserId, restaurantId, updateData)
        );
        
        assertEquals("Permission denied. You can only edit your own restaurants.", exception.getMessage());
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldHandleNullUpdateData() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        
        // Act & Assert
        assertThrows(
            NullPointerException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, null)
        );
        
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldHandleNullRestaurantId() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setId(null);
        
        // Act & Assert
        assertThrows(
            BusinessException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, null, updateData)
        );
        
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldNotUpdateWithNullFields() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName(null);
        updateData.setCuisineType(null);
        updateData.setOpeningHours(null);
        updateData.setDescription(null);
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);
        
        // Assert - Original values should be preserved
        assertEquals("Original Name", result.getName());
        assertEquals("ITALIAN", result.getCuisineType());
        assertEquals("09:00-22:00", result.getOpeningHours());
        assertEquals("Original Description", result.getDescription());
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldHandleEmptyStrings() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("");
        updateData.setCuisineType("");
        updateData.setOpeningHours("");
        updateData.setDescription("");
        
        // Act & Assert - Empty name should throw validation exception
        assertThrows(
            Exception.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData)
        );
        
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldHandleRepositoryUpdateFailure() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        updateData.setName("New Name");
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        assertThrows(
            RuntimeException.class,
            () -> updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData)
        );
        
        verify(restaurantRepository).update(existingRestaurant);
    }

    @Test
    void shouldNotUpdateImmutableFields() {
        // Arrange
        UUID newId = UUID.randomUUID();
        updateData.setId(newId);
        updateData.setUserOwnerId(UUID.randomUUID());
        
        // Mock user as owner
        User ownerUser = new User();
        ownerUser.setId(existingRestaurant.getUserOwnerId());
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(existingRestaurant.getUserOwnerId())).thenReturn(Optional.of(ownerUser));
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        // Use the authenticated user ID as the restaurant owner ID for this test
        Restaurant result = updateRestaurantUseCase.execute(existingRestaurant.getUserOwnerId(), restaurantId, updateData);
        
        // Assert - Immutable fields should not be updated
        assertEquals(restaurantId, result.getId()); // ID should remain the same
        assertEquals(existingRestaurant.getUserOwnerId(), result.getUserOwnerId()); // Owner ID should remain unchanged
    }

    @Test
    void shouldAllowAdminToUpdateAnyRestaurant() {
        // Arrange
        UUID adminUserId = UUID.randomUUID();
        User adminUser = new User();
        adminUser.setId(adminUserId);
        UserType adminType = new UserType();
        adminType.setName("ADMIN");
        adminUser.setUserType(adminType);
        when(userRepository.findById(adminUserId)).thenReturn(Optional.of(adminUser));
        
        updateData.setName("Admin Updated Name");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Restaurant result = updateRestaurantUseCase.execute(adminUserId, restaurantId, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Admin Updated Name", result.getName());
        verify(restaurantRepository).update(any(Restaurant.class));
    }

    @Test
    void shouldHandleUserNotFound() {
        // Arrange
        UUID unknownUserId = UUID.randomUUID();
        when(userRepository.findById(unknownUserId)).thenReturn(Optional.empty());
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        
        updateData.setName("Updated Name");

        // Act & Assert
        assertThrows(
            UnauthorizedAccessException.class,
            () -> updateRestaurantUseCase.execute(unknownUserId, restaurantId, updateData)
        );
        
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldHandleUserWithNullUserType() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User userWithNullType = new User();
        userWithNullType.setId(userId);
        userWithNullType.setUserType(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithNullType));
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        
        updateData.setName("Updated Name");

        // Act & Assert
        assertThrows(
            UnauthorizedAccessException.class,
            () -> updateRestaurantUseCase.execute(userId, restaurantId, updateData)
        );
        
        verify(restaurantRepository, never()).update(any());
    }

    @Test
    void shouldUpdateRestaurantWithNewAddress() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        Addresses newAddress = new Addresses(null, "New St", "456", "Suite 10", "Uptown", "NewCity", "NC", "98765432");
        updateData.setAddress(newAddress);
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(addressRepository.findByOwner(restaurantId, "RESTAURANT")).thenReturn(Collections.emptyList());
        when(addressRepository.save(any(Addresses.class), eq(restaurantId), eq("RESTAURANT"))).thenReturn(newAddress);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);

        // Assert
        assertNotNull(result);
        verify(addressRepository).save(any(Addresses.class), eq(restaurantId), eq("RESTAURANT"));
        verify(restaurantRepository).update(any(Restaurant.class));
    }

    @Test
    void shouldUpdateExistingAddress() {
        // Arrange
        UUID authenticatedUserId = existingRestaurant.getUserOwnerId();
        User ownerUser = new User();
        ownerUser.setId(authenticatedUserId);
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser.setUserType(ownerType);
        when(userRepository.findById(authenticatedUserId)).thenReturn(Optional.of(ownerUser));
        
        Addresses existingAddress = new Addresses(UUID.randomUUID(), "Old St", "123", "Apt 1", "Downtown", "City", "ST", "12345678");
        Addresses updatedAddress = new Addresses(null, "Updated St", "789", "Suite 5", "Midtown", "City", "ST", "87654321");
        updateData.setAddress(updatedAddress);
        
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(addressRepository.findByOwner(restaurantId, "RESTAURANT")).thenReturn(List.of(existingAddress));
        when(addressRepository.save(any(Addresses.class), eq(restaurantId), eq("RESTAURANT"))).thenReturn(updatedAddress);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Restaurant result = updateRestaurantUseCase.execute(authenticatedUserId, restaurantId, updateData);

        // Assert
        assertNotNull(result);
        verify(addressRepository).save(any(Addresses.class), eq(restaurantId), eq("RESTAURANT"));
        verify(restaurantRepository).update(any(Restaurant.class));
    }

    @Test
    void shouldBeCaseInsensitiveForAdminCheck() {
        // Arrange
        UUID adminUserId = UUID.randomUUID();
        User adminUser = new User();
        adminUser.setId(adminUserId);
        UserType adminType = new UserType();
        adminType.setName("admin"); // lowercase
        adminUser.setUserType(adminType);
        when(userRepository.findById(adminUserId)).thenReturn(Optional.of(adminUser));
        
        updateData.setName("Admin Updated Name");
        when(restaurantRepository.findById(restaurantId)).thenReturn(existingRestaurant);
        when(restaurantRepository.update(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Restaurant result = updateRestaurantUseCase.execute(adminUserId, restaurantId, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Admin Updated Name", result.getName());
        verify(restaurantRepository).update(any(Restaurant.class));
    }
}
