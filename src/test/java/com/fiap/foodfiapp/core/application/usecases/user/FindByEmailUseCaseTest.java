package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.usecases.user.impl.FindByEmailUseCaseImpl;
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
class FindByEmailUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindByEmailUseCaseImpl findByEmailUseCase;

    private User user;
    private UserType customerType;
    private List<Addresses> addresses;
    private String email;

    @BeforeEach
    void setUp() {
        email = "john@example.com";
        customerType = new UserType(UUID.randomUUID(), "CUSTOMER");
        
        user = new User(
            UUID.randomUUID(),
            "John Doe",
            email,
            "john123",
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
    void shouldFindUserByEmailWithAddresses() {
        // Arrange
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        User result = findByEmailUseCase.execute(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("John Doe", result.getName());
        assertNotNull(result.getAddress());
        assertEquals(1, result.getAddress().size());
        verify(userRepository).findByEmail(email);
        verify(addressRepository).findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        // Arrange
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> findByEmailUseCase.execute(nonExistentEmail)
        );
        
        assertTrue(exception.getMessage().contains(nonExistentEmail));
        verify(userRepository).findByEmail(nonExistentEmail);
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldFindUserWithoutAddresses() {
        // Arrange
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(Collections.emptyList());

        // Act
        User result = findByEmailUseCase.execute(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertNotNull(result.getAddress());
        assertTrue(result.getAddress().isEmpty());
    }

    @Test
    void shouldHandleUserWithNullId() {
        // Arrange
        user.setId(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = findByEmailUseCase.execute(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
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
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(multipleAddresses);

        // Act
        User result = findByEmailUseCase.execute(email);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getAddress().size());
    }

    @Test
    void shouldHandleDifferentEmailFormats() {
        // Arrange
        String uppercaseEmail = "JOHN@EXAMPLE.COM";
        when(userRepository.findByEmail(uppercaseEmail)).thenReturn(Optional.of(user));
        when(addressRepository.findByOwner(user.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        User result = findByEmailUseCase.execute(uppercaseEmail);

        // Assert
        assertNotNull(result);
        verify(userRepository).findByEmail(uppercaseEmail);
    }
}
