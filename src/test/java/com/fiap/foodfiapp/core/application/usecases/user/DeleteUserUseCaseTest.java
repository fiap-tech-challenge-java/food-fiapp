package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.user.impl.DeleteUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private AddressRepository addressRepository;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    private DeleteUserUseCaseImpl deleteUserUseCase;

    private UUID userId;
    private User existingUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteUserUseCase = new DeleteUserUseCaseImpl(userRepository, addressRepository);
        
        userId = UUID.randomUUID();
        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Test User");
        existingUser.setEmail("test@example.com");
        existingUser.setLogin("testuser");
    }

    @Test
    void shouldSoftDeleteExistingUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(Collections.emptyList());
        when(userRepository.save(any())).thenReturn(existingUser);

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(addressRepository).findByOwner(userId, "USER");
        verify(userRepository).save(argThat(user -> 
            user.getId().equals(userId) && 
            Boolean.FALSE.equals(user.getIsActive())
        ));
    }

    @Test
    void shouldNotThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(addressRepository, never()).findByOwner(any(), any());
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void shouldDeactivateCorrectUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(Collections.emptyList());
        when(userRepository.save(any())).thenReturn(existingUser);

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(userRepository).save(argThat(user -> 
            user.getId().equals(userId) && 
            Boolean.FALSE.equals(user.getIsActive())
        ));
    }

    @Test
    void shouldHandleNullId() {
        // Act
        deleteUserUseCase.execute(null);
        
        // Assert - Should not throw, just return early
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotProceedWithDeletionIfUserDoesNotExist() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        deleteUserUseCase.execute(nonExistentId);

        // Assert
        verify(addressRepository, never()).findByOwner(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotDeactivateAlreadyInactiveUser() {
        // Arrange
        existingUser.setIsActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldDeactivateUserAddresses() {
        // Arrange
        Addresses address1 = new Addresses();
        address1.setIsActive(true);
        Addresses address2 = new Addresses();
        address2.setIsActive(true);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(List.of(address1, address2));
        when(userRepository.save(any())).thenReturn(existingUser);

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(addressRepository).findByOwner(userId, "USER");
        verify(addressRepository, times(2)).save(any(Addresses.class), eq(userId), eq("USER"));
    }

    @Test
    void shouldNotDeactivateAlreadyInactiveAddresses() {
        // Arrange
        Addresses activeAddress = new Addresses();
        activeAddress.setIsActive(true);
        Addresses inactiveAddress = new Addresses();
        inactiveAddress.setIsActive(false);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(List.of(activeAddress, inactiveAddress));
        when(userRepository.save(any())).thenReturn(existingUser);

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(addressRepository).findByOwner(userId, "USER");
        // Should only save the active address, not the inactive one
        verify(addressRepository, times(1)).save(argThat(addr -> Boolean.FALSE.equals(addr.getIsActive())), eq(userId), eq("USER"));
    }

    @Test
    void shouldHandleUserWithNoAddresses() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressRepository.findByOwner(userId, "USER")).thenReturn(Collections.emptyList());
        when(userRepository.save(any())).thenReturn(existingUser);

        // Act
        deleteUserUseCase.execute(userId);

        // Assert
        verify(userRepository).save(argThat(user -> Boolean.FALSE.equals(user.getIsActive())));
        verify(addressRepository).findByOwner(userId, "USER");
        verify(addressRepository, never()).save(any(Addresses.class), any(UUID.class), anyString());
    }
}
