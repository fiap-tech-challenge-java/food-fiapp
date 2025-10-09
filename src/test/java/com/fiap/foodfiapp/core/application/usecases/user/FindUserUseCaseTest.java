package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.user.impl.FindUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindUserUseCaseImpl findUserUseCase;

    private UUID userId;
    private User testUser;
    private List<Addresses> testAddresses;

    private final String testEmail = "test1@example.com";
    private final String testUsername = "testuser1";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEmail(testEmail);
        testUser.setLogin(testUsername);
        testUser.setPassword("password123");
        
        // Setup test addresses
        testAddresses = List.of(
            new Addresses(UUID.randomUUID(), "Main St", "123", "Apt 4", "Downtown", "City", "ST", "12345")
        );
    }

    @Test
    void shouldFindUserById() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(testAddresses);

        // Act
        Optional<User> result = findUserUseCase.execute(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        assertEquals(testAddresses, result.get().getAddress());
        verify(userRepository).findById(userId);
        verify(addressRepository).findByOwner(userId, "USER");
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = findUserUseCase.execute(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findById(userId);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldReturnEmptyWhenUserIdIsNull() {
        // Act
        Optional<User> result = findUserUseCase.execute(null);
        
        // Assert
        assertTrue(result.isEmpty());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldLoadUserAddresses() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(testAddresses);

        // Act
        Optional<User> result = findUserUseCase.execute(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testAddresses, result.get().getAddress());
        verify(userRepository).findById(userId);
        verify(addressRepository).findByOwner(userId, "USER");
    }

    @Test
    void shouldNotLoadAddressesForNonExistentUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = findUserUseCase.execute(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findById(userId);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldHandleEmptyAddressList() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(Collections.emptyList());

        // Act
        Optional<User> result = findUserUseCase.execute(userId);

        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get().getAddress().isEmpty());
        verify(userRepository).findById(userId);
        verify(addressRepository).findByOwner(userId, "USER");
    }

    @Test
    void shouldReturnEmptyWhenUserIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = findUserUseCase.execute(nonExistentId);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findById(nonExistentId);
    }

    @Test
    void shouldNotCallRepositoryWithNullId() {
        // Act
        Optional<User> result = findUserUseCase.execute(null);

        // Assert
        assertFalse(result.isPresent());
        verifyNoInteractions(userRepository, addressRepository);
    }
}
