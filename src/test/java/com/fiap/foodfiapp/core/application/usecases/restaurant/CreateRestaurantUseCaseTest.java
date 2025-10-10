package com.fiap.foodfiapp.core.application.usecases.restaurant;

import com.fiap.foodfiapp.core.application.usecases.restaurant.impl.CreateRestaurantUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private AddressRepository addressRepository;

    @Captor
    private ArgumentCaptor<Restaurant> restaurantCaptor;

    private CreateRestaurantUseCaseImpl createRestaurantUseCase;

    private Restaurant restaurant;
    private User ownerUser;
    private User nonOwnerUser;
    private final UUID ownerId = UUID.randomUUID();
    private final String restaurantName = "Test Restaurant";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createRestaurantUseCase = new CreateRestaurantUseCaseImpl(restaurantRepository, userRepository, addressRepository);

        // Setup owner user
        UserType ownerType = new UserType();
        ownerType.setName("OWNER");
        ownerUser = new User();
        ownerUser.setId(ownerId);
        ownerUser.setUserType(ownerType);

        // Setup non-owner user
        UserType customerType = new UserType();
        customerType.setName("CUSTOMER");
        nonOwnerUser = new User();
        nonOwnerUser.setId(UUID.randomUUID());
        nonOwnerUser.setUserType(customerType);

        // Setup restaurant
        restaurant = new Restaurant(
            UUID.randomUUID(),
            restaurantName,
            "Test Cuisine",
            "09:00-22:00",
            ownerId,
            "Test Description",
            null
        );
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = createRestaurantUseCase.execute(ownerId, restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantName, result.getName());
        assertEquals(restaurantName, result.getName());
        
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository).findByNameAndUser(restaurantName, ownerId);
        verify(restaurantRepository).save(restaurantCaptor.capture());
        
        Restaurant savedRestaurant = restaurantCaptor.getValue();
        assertEquals(restaurantName, restaurant.getName());
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        assertEquals("User not found with id: " + ownerId, exception.getMessage());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(nonOwnerUser));

        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
            UnauthorizedAccessException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        assertEquals("Only users with role 'OWNER' or 'ADMIN' can create restaurants", exception.getMessage());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNameExistsForSameOwner() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(new Restaurant());

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        assertEquals("A restaurant with name '" + restaurantName + "' already exists for this owner", 
                     exception.getMessage());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldHandleOwnerWithDifferentCaseRoleName() {
        // Arrange
        UserType ownerType = new UserType();
        ownerType.setName("dOnO dE rEsTaUrAnTe"); // Different case and spacing
        ownerUser.setUserType(ownerType);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act & Assert
        assertDoesNotThrow(() -> createRestaurantUseCase.execute(ownerId, restaurant));
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> createRestaurantUseCase.execute(ownerId, null));
        verify(userRepository, never()).findById(any());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNameIsNull() {
        // Arrange
        restaurant.setName(null);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        // Act & Assert
        assertThrows(
            BusinessException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldAllowAdminToCreateRestaurant() {
        // Arrange
        User adminUser = new User();
        adminUser.setId(ownerId);
        UserType adminType = new UserType();
        adminType.setName("ADMIN");
        adminUser.setUserType(adminType);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(adminUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = createRestaurantUseCase.execute(ownerId, restaurant);

        // Assert
        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserHasNullUserType() {
        // Arrange
        User userWithNullType = new User();
        userWithNullType.setId(ownerId);
        userWithNullType.setUserType(null);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(userWithNullType));

        // Act & Assert
        assertThrows(
            UnauthorizedAccessException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldCreateRestaurantWithAddress() {
        // Arrange
        Addresses address = new Addresses(null, "Main St", "123", "Apt 4", "Downtown", "City", "ST", "12345678");
        restaurant.setAddress(address);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString())).thenReturn(address);

        // Act
        Restaurant result = createRestaurantUseCase.execute(ownerId, restaurant);

        // Assert
        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(addressRepository).save(any(Addresses.class), any(UUID.class), eq("RESTAURANT"));
    }

    @Test
    void shouldCreateRestaurantWithoutAddress() {
        // Arrange
        restaurant.setAddress(null);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = createRestaurantUseCase.execute(ownerId, restaurant);

        // Assert
        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNameIsBlank() {
        // Arrange
        restaurant.setName("   ");
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        // Act & Assert
        assertThrows(
            BusinessException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldBeCaseInsensitiveForAdminCheck() {
        // Arrange
        User adminUser = new User();
        adminUser.setId(ownerId);
        UserType adminType = new UserType();
        adminType.setName("admin"); // lowercase
        adminUser.setUserType(adminType);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(adminUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = createRestaurantUseCase.execute(ownerId, restaurant);

        // Assert
        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldHandleUserWithNullUserType() {
        // Arrange
        User userWithNullType = new User();
        userWithNullType.setId(ownerId);
        userWithNullType.setUserType(null); // Null userType
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(userWithNullType));

        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
            UnauthorizedAccessException.class,
            () -> createRestaurantUseCase.execute(ownerId, restaurant)
        );
        
        assertTrue(exception.getMessage().contains("Only users with role 'OWNER' or 'ADMIN' can create restaurants"));
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldAllowDonoDeRestauranteRoleToCreateRestaurant() {
        // Arrange
        User donoUser = new User();
        donoUser.setId(ownerId);
        UserType donoType = new UserType();
        donoType.setName("DONO DE RESTAURANTE");
        donoUser.setUserType(donoType);
        
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(donoUser));
        when(restaurantRepository.findByNameAndUser(restaurantName, ownerId)).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = createRestaurantUseCase.execute(ownerId, restaurant);

        // Assert
        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
    }
}
