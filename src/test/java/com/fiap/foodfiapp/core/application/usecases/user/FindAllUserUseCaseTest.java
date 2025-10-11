package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.usecases.user.impl.FindAllUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindAllUserUseCaseImpl findAllUserUseCase;

    private User user1;
    private User user2;
    private UserType customerType;
    private List<Addresses> addresses;

    @BeforeEach
    void setUp() {
        customerType = new UserType(UUID.randomUUID(), "CUSTOMER");
        
        user1 = new User(
            UUID.randomUUID(),
            "John Doe",
            "john@example.com",
            "john123",
            "12345678900",
            null,
            customerType,
            true,
            null,
            null,
            "password123"
        );
        
        user2 = new User(
            UUID.randomUUID(),
            "Jane Smith",
            "jane@example.com",
            "jane456",
            "98765432100",
            null,
            customerType,
            true,
            null,
            null,
            "password456"
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
    void shouldReturnAllUsersWithAddresses() {
        // Arrange
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(addressRepository.findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(addresses);

        // Act
        List<User> result = findAllUserUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(addressRepository, times(2)).findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = findAllUserUseCase.execute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldLoadAddressesForEachUser() {
        // Arrange
        List<User> users = Collections.singletonList(user1);
        when(userRepository.findAll()).thenReturn(users);
        when(addressRepository.findByOwner(user1.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        List<User> result = findAllUserUseCase.execute();

        // Assert
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getAddress());
        assertEquals(1, result.get(0).getAddress().size());
        verify(addressRepository).findByOwner(user1.getId(), AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldHandleUsersWithoutAddresses() {
        // Arrange
        List<User> users = Collections.singletonList(user1);
        when(userRepository.findAll()).thenReturn(users);
        when(addressRepository.findByOwner(user1.getId(), AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(Collections.emptyList());

        // Act
        List<User> result = findAllUserUseCase.execute();

        // Assert
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getAddress());
        assertTrue(result.get(0).getAddress().isEmpty());
    }

    @Test
    void shouldHandleUserWithNullId() {
        // Arrange
        User userWithNullId = new User(
            null,
            "Test User",
            "test@example.com",
            "test123",
            "11122233344",
            null,
            customerType,
            true,
            null,
            null,
            "password"
        );
        List<User> users = Collections.singletonList(userWithNullId);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = findAllUserUseCase.execute();

        // Assert
        assertEquals(1, result.size());
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldHandleNullUser() {
        // Arrange
        List<User> users = Arrays.asList(user1, null, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(addressRepository.findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(addresses);

        // Act
        List<User> result = findAllUserUseCase.execute();

        // Assert
        assertEquals(3, result.size());
        verify(addressRepository, times(2)).findByOwner(any(UUID.class), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }
}
