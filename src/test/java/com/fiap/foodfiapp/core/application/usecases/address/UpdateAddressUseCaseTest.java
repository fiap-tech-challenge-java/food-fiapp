package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.impl.UpdateAddressesUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateAddressUseCaseTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ValidateOwnerUseCase validateOwnerUseCase;

    @InjectMocks
    private UpdateAddressesUseCaseImpl updateAddressUseCase;

    private UUID addressId;
    private UUID ownerId;
    private String ownerType;
    private Addresses existingAddress;
    private Addresses addressUpdates;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        addressId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        ownerType = "USER";
        
        // Setup existing address
        existingAddress = new Addresses(
            addressId,
            "Old Street",
            "123",
            "Apt 1",
            "Downtown",
            "Old City",
            "OS",
            "12345-678"
        );
        
        // Setup address updates - use null ID since it will be set from the existing address
        addressUpdates = new Addresses(
            null, // ID will be set from existing address
            "New Street",
            "456",
            "Apt 2",
            "Uptown",
            "New City",
            "NS",
            "98765-432"
        );
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        // Arrange
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
            .thenAnswer(invocation -> {
                Addresses addr = invocation.getArgument(0);
                return addr; // Just return the same object for testing
            });

        // Act
        Addresses updatedAddress = updateAddressUseCase.execute(addressId, addressUpdates, ownerId, ownerType);

        // Assert
        assertNotNull(updatedAddress);
        assertEquals(addressId, updatedAddress.getId());
        assertEquals("New Street", updatedAddress.getPublicPlace());
        assertEquals("New City", updatedAddress.getCity());
        assertEquals("NS", updatedAddress.getState());
        assertEquals("98765-432", updatedAddress.getPostalCode());
        assertTrue(updatedAddress.getIsActive());
        
        verify(addressRepository).findByIdAndOwnerId(addressId, ownerId);
        verify(addressRepository).save(any(Addresses.class), eq(ownerId), eq(ownerType));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        // Arrange
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AddressNotFoundException.class, 
            () -> updateAddressUseCase.execute(addressId, addressUpdates, ownerId, ownerType));
        
        verify(addressRepository).findByIdAndOwnerId(addressId, ownerId);
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldPreserveIdFromExistingAddress() {
        // Arrange
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Addresses.class), eq(ownerId), eq(ownerType)))
            .thenAnswer(invocation -> {
                Addresses savedAddress = invocation.getArgument(0);
                savedAddress.setId(addressId); // Simulate ID assignment
                return savedAddress;
            });

        // Act
        Addresses updatedAddress = updateAddressUseCase.execute(addressId, addressUpdates, ownerId, ownerType);

        // Assert
        assertNotNull(updatedAddress);
        assertEquals(addressId, updatedAddress.getId());
        verify(addressRepository).findByIdAndOwnerId(addressId, ownerId);
        verify(addressRepository).save(any(Addresses.class), eq(ownerId), eq(ownerType));
        verify(addressRepository).save(argThat(addr -> addressId.equals(addr.getId())), any(), any());
    }

    @Test
    void shouldNotAllowChangingOwnerId() {
        // Arrange
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Addresses.class), any(), any()))
            .thenAnswer(invocation -> {
                Addresses addr = invocation.getArgument(0);
                // The owner ID is set by the repository, not in the entity
                return addr;
            });

        // Act
        Addresses updatedAddress = updateAddressUseCase.execute(addressId, addressUpdates, ownerId, ownerType);

        // Assert - The owner ID is managed by the repository, not the entity
        verify(addressRepository).save(any(Addresses.class), eq(ownerId), eq(ownerType));
    }

    @Test
    void shouldNotAllowChangingOwnerType() {
        // Arrange
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Addresses.class), any(), any()))
            .thenAnswer(invocation -> {
                Addresses addr = invocation.getArgument(0);
                // The owner type is set by the repository, not in the entity
                return addr;
            });

        // Act
        Addresses updatedAddress = updateAddressUseCase.execute(addressId, addressUpdates, ownerId, ownerType);

        // Assert - The owner type is managed by the repository, not the entity
        verify(addressRepository).save(any(Addresses.class), eq(ownerId), eq(ownerType));
    }

    @Test
    void shouldHandleNullUpdates() {
        // Arrange
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        
        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> updateAddressUseCase.execute(addressId, null, ownerId, ownerType));
        
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldHandleNullOwnerId() {
        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> updateAddressUseCase.execute(addressId, addressUpdates, null, ownerType));
        
        verify(addressRepository, never()).findById(any());
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldHandleNullOwnerType() {
        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> updateAddressUseCase.execute(addressId, addressUpdates, ownerId, null));
        
        verify(addressRepository, never()).findByIdAndOwnerId(any(), any());
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldHandleEmptyOwnerType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> updateAddressUseCase.execute(addressId, addressUpdates, ownerId, ""));
        
        verify(addressRepository, never()).findByIdAndOwnerId(any(), any());
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldOnlyUpdateProvidedFields() {
        // Arrange
        Addresses partialUpdates = new Addresses(
            null, // id will be set by the repository
            "Old Street", // publicPlace - keeping same
            "123", // number - keeping same
            "Apt 1", // complement - keeping same
            "Downtown", // neighborhood - keeping same
            "Updated City", // city - only updating this field
            "OS", // state - keeping same
            "12345-678"  // postalCode - keeping same
        );
        
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Addresses.class), eq(ownerId), eq(ownerType)))
            .thenAnswer(invocation -> {
                Addresses saved = invocation.getArgument(0);
                saved.setId(addressId); // Simulate ID assignment
                return saved;
            });

        // Act
        Addresses updatedAddress = updateAddressUseCase.execute(addressId, partialUpdates, ownerId, ownerType);

        // Assert
        assertNotNull(updatedAddress);
        assertEquals("Updated City", updatedAddress.getCity());
        assertEquals("Old Street", existingAddress.getPublicPlace()); // Should remain unchanged
        assertEquals("OS", existingAddress.getState()); // Should remain unchanged
        assertEquals("12345-678", existingAddress.getPostalCode()); // Should remain unchanged
    }
}
