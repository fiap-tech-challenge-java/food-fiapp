package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.CpfAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.LoginAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private AddressRepository addressRepository;

    private CreateUserUseCaseImpl createUserUseCase;

    private User user;
    private UserType userType;
    private Addresses address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCase = new CreateUserUseCaseImpl(userRepository, userTypeRepository, addressRepository);

        userType = new UserType();
        userType.setUuid(UUID.randomUUID());
        userType.setName("CUSTOMER");

// Create address using constructor
        address = new Addresses(
            UUID.randomUUID(),  // id
            "Test Street",      // publicPlace
            "123",              // number
            "Apt 4B",          // complement
            "Downtown",         // neighborhood
            "Test City",        // city
            "TS",               // state
            "12345678"          // postalCode
        );

        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setCpf("11144477735"); // Valid CPF for testing
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setUserType(userType);
        user.setAddress(List.of(address));
    }

    @Test
    void shouldCreateNewUserSuccessfully() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userType.getUuid())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
                .thenReturn(address);

        // Act
        User result = createUserUseCase.execute(user);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
        verify(addressRepository).save(any(Addresses.class), any(UUID.class), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldReviveInactiveUserByEmail() {
        // Arrange
        User inactiveUser = new User();
        inactiveUser.setId(UUID.randomUUID());
        inactiveUser.setEmail(user.getEmail());
        inactiveUser.setIsActive(false);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
                .thenReturn(address);

        // Act
        User result = createUserUseCase.execute(user);

        // Assert
        assertTrue(result.getIsActive());
        assertEquals(user.getName(), result.getName());
        verify(userRepository, never()).findByCpf(anyString());
        verify(userRepository, never()).findByLogin(anyString());
    }

    @Test
    void shouldReviveInactiveUserByCpf() {
        // Arrange
        User inactiveUser = new User();
        inactiveUser.setId(UUID.randomUUID());
        inactiveUser.setCpf(user.getCpf());
        inactiveUser.setIsActive(false);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
                .thenReturn(address);

        // Act
        User result = createUserUseCase.execute(user);

        // Assert
        assertTrue(result.getIsActive());
        assertEquals(user.getName(), result.getName());
        verify(userRepository, never()).findByLogin(anyString());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail(user.getEmail());
        existingUser.setIsActive(true);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(CpfAlreadyExistsException.class, () -> createUserUseCase.execute(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(LoginAlreadyExistsException.class, () -> createUserUseCase.execute(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userType.getUuid())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserTypeNotFoundException.class, () -> createUserUseCase.execute(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldHandleNullAddress() {
        // Arrange
        user.setAddress(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userType.getUuid())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });

        // Act & Assert
        assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
        verify(userRepository, never()).save(any(User.class));
        verify(addressRepository, never()).save(any(Addresses.class), any(UUID.class), anyString());
    }
}
