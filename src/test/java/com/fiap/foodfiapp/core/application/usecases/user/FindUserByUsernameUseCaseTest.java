package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.usecases.user.impl.FindUserByUsernameUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByUsernameUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindUserByUsernameUseCaseImpl findUserByUsernameUseCase;

    private User user;
    private UserType customerType;
    private List<Addresses> addresses;
    private String username;

    @BeforeEach
    void setUp() {
        username = "john123";
        customerType = new UserType(UUID.randomUUID(), "CUSTOMER");
        
        user = new User(
            UUID.randomUUID(),
            "John Doe",
            "john@example.com",
            username,
            "12345678900",
            null,
            customerType,
            true,
            null,
            null,
            "password123"
        );
        
        Addresses address = new Addresses(
            UUID.randomUUID(),
            "Main Street",
            "123",
            "Apt 1",
            "Downtown",
            "City",
            "ST",
            "12345-678"
        );
        
        addresses = Collections.singletonList(address);
    }

    @Test
    void shouldFindUserByUsernameWithAddresses() {
        // Arrange
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        User result = findUserByUsernameUseCase.execute(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getLogin());
        assertEquals("John Doe", result.getName());
        assertNotNull(result.getAddress());
        assertEquals(1, result.getAddress().size());
        verify(userRepository).findByLogin(username);
        verify(addressRepository).findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        // Arrange
        String nonExistentUsername = "nonexistent123";
        when(userRepository.findByLogin(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> findUserByUsernameUseCase.execute(nonExistentUsername)
        );
        
        assertTrue(exception.getMessage().contains(nonExistentUsername));
        verify(userRepository).findByLogin(nonExistentUsername);
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldFindUserWithoutAddresses() {
        // Arrange
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(Collections.emptyList());

        // Act
        User result = findUserByUsernameUseCase.execute(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getLogin());
        assertNotNull(result.getAddress());
        assertTrue(result.getAddress().isEmpty());
    }

    @Test
    void shouldHandleUserWithNullId() {
        // Arrange
        user.setId(null);
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));

        // Act
        User result = findUserByUsernameUseCase.execute(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getLogin());
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldFindUserWithMultipleAddresses() {
        // Arrange
        Addresses address2 = new Addresses(
            UUID.randomUUID(),
            "Second Street",
            "456",
            "Apt 2",
            "Uptown",
            "City",
            "ST",
            "98765-432"
        );
        List<Addresses> multipleAddresses = Arrays.asList(addresses.get(0), address2);
        
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(multipleAddresses);

        // Act
        User result = findUserByUsernameUseCase.execute(username);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getAddress().size());
    }

    @Test
    void shouldHandleDifferentUsernameFormats() {
        // Arrange
        String uppercaseUsername = "JOHN123";
        when(userRepository.findByLogin(uppercaseUsername)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        User result = findUserByUsernameUseCase.execute(uppercaseUsername);

        // Assert
        assertNotNull(result);
        verify(userRepository).findByLogin(uppercaseUsername);
    }

    @Test
    void shouldLoadAddressesOnlyOnce() {
        // Arrange
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        findUserByUsernameUseCase.execute(username);

        // Assert
        verify(addressRepository, times(1)).findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldHandleNullUser() {
        // Arrange
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        User result = findUserByUsernameUseCase.execute(username);

        // Assert
        assertNotNull(result);
        verify(addressRepository).findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription());
    }
}
