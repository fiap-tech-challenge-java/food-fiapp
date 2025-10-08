package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.CpfAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.LoginAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private AddressRepository addressRepository;

    private CreateUserUseCaseImpl createUserUseCase;

    private User user;
    private UserType userType;
    private Address address;
    private UUID userTypeUuid;
    private UUID userId;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCaseImpl(userRepository, userTypeRepository, addressRepository);

        userTypeUuid = UUID.randomUUID();
        userId = UUID.randomUUID();

        userType = new UserType();
        userType.setUuid(userTypeUuid);
        userType.setName("CLIENT");

        address = new Address();
        address.setId(UUID.randomUUID());
        address.setPublicPlace("Rua Teste");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setPostalCode("01001-000");

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setCpf("12345678901");
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setUserType(userType);
        user.setAddresses(List.of(address));
        user.setIsActive(true);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userRepository.save(user)).thenReturn(user);
        when(addressRepository.save(any(Address.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
                .thenReturn(address);

        // When
        User result = createUserUseCase.execute(user);

        // Then
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(1, result.getAddresses().size());

        verify(userRepository, times(2)).findByEmail(user.getEmail()); // Called twice: revival check + duplicate validation
        verify(userRepository).findByLogin(user.getLogin());
        verify(userTypeRepository).findById(userTypeUuid);
        verify(userRepository).save(user);
        verify(addressRepository).save(any(Address.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldThrowExceptionWhenNoAddressProvided() {
        // Given
        user.setAddresses(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals("At least one address is required for user creation.", exception.getMessage());

        // Verify no repository interactions
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userTypeRepository);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldThrowExceptionWhenEmptyAddressList() {
        // Given
        user.setAddresses(Collections.emptyList());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals("At least one address is required for user creation.", exception.getMessage());

        // Verify no repository interactions
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userTypeRepository);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        User existingUser = new User();
        existingUser.setIsActive(true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals("User with this email already exists.", exception.getMessage());
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        User existingUser = new User();
        existingUser.setIsActive(true);
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.of(existingUser));

        // When & Then
        CpfAlreadyExistsException exception = assertThrows(CpfAlreadyExistsException.class, () -> {
            createUserUseCase.execute(user);
        });

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByCpf(user.getCpf());
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        // When & Then
        LoginAlreadyExistsException exception = assertThrows(LoginAlreadyExistsException.class, () -> {
            createUserUseCase.execute(user);
        });

        verify(userRepository).findByLogin(user.getLogin());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeUuid)).thenReturn(Optional.empty());

        // When & Then
        UserTypeNotFoundException exception = assertThrows(UserTypeNotFoundException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals("User type not found.", exception.getMessage());
        verify(userTypeRepository).findById(userTypeUuid);
    }

    @Test
    void shouldThrowExceptionWhenUserTypeIsNull() {
        // Given
        user.setUserType(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals("User type is required.", exception.getMessage());
    }

    @Test
    void shouldReviveInactiveUserWithSameEmail() {
        // Given
        User inactiveUser = new User();
        inactiveUser.setId(userId);
        inactiveUser.setEmail(user.getEmail());
        inactiveUser.setIsActive(false);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(inactiveUser)).thenReturn(inactiveUser);
        when(addressRepository.save(any(Address.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
                .thenReturn(address);

        // When
        User result = createUserUseCase.execute(user);

        // Then
        assertNotNull(result);
        assertTrue(result.getIsActive());
        assertEquals(user.getName(), result.getName());
        assertEquals(1, result.getAddresses().size());

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(inactiveUser);
        verify(addressRepository).save(any(Address.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldReviveInactiveUserWithSameCpf() {
        // Given
        User inactiveUser = new User();
        inactiveUser.setId(userId);
        inactiveUser.setCpf(user.getCpf());
        inactiveUser.setIsActive(false);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(user.getCpf())).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(inactiveUser)).thenReturn(inactiveUser);
        when(addressRepository.save(any(Address.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
                .thenReturn(address);

        // When
        User result = createUserUseCase.execute(user);

        // Then
        assertNotNull(result);
        assertTrue(result.getIsActive());
        assertEquals(user.getName(), result.getName());
        assertEquals(1, result.getAddresses().size());

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByCpf(user.getCpf());
        verify(userRepository).save(inactiveUser);
        verify(addressRepository).save(any(Address.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }
}
