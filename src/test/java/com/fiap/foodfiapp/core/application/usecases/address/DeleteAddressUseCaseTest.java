package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.impl.DeleteAddressesUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteAddressUseCaseTest {

    @Mock
    private AddressRepository addressRepository;
    
    @Mock
    private ValidateOwnerUseCase validateOwnerUseCase;

    @InjectMocks
    private DeleteAddressesUseCaseImpl deleteAddressUseCase;

    private UUID ownerId;
    private UUID addressId;
    private String ownerType;
    private Addresses address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        ownerId = UUID.randomUUID();
        addressId = UUID.randomUUID();
        ownerType = "USER";
        
        // Create address using the constructor with all required fields
        address = new Addresses(
            addressId,
            "Rua Teste",
            "123",
            "Apt 1",
            "Centro",
            "São Paulo",
            "SP",
            "01001000"
        );
        
        // Mock the validateOwnerUseCase to do nothing by default
        doNothing().when(validateOwnerUseCase).execute(any(), any());
        
        // Default mock for findByIdAndOwnerId - returns empty by default
        when(addressRepository.findByIdAndOwnerId(any(), any())).thenReturn(Optional.empty());
        
        // Default mock for findByOwner - returns empty list by default
        when(addressRepository.findByOwner(any(), any())).thenReturn(new ArrayList<>());
    }

    @Test
    void shouldDeleteAddressSuccessfully() {
        // Arrange
        UUID anotherAddressId = UUID.randomUUID();
        Addresses anotherActiveAddress = new Addresses(
            anotherAddressId,
            "Another Street",
            "456",
            "Apt 2",
            "Vila Mariana",
            "São Paulo",
            "SP",
            "04010010"
        );

        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(address));
        when(addressRepository.findByOwner(ownerId, ownerType))
            .thenReturn(List.of(address, anotherActiveAddress));

        // Act
        deleteAddressUseCase.execute(ownerId, addressId, ownerType);

        // Assert
        verify(addressRepository).delete(addressId);
        verify(validateOwnerUseCase).execute(ownerId, ownerType);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        // Arrange - findByIdAndOwnerId returns empty by default

        // Act & Assert
        assertThrows(AddressNotFoundException.class,
            () -> deleteAddressUseCase.execute(ownerId, addressId, ownerType));
        
        verify(addressRepository, never()).delete(any());
        verify(validateOwnerUseCase).execute(ownerId, ownerType);
    }

    @Test
    void shouldThrowExceptionWhenDeletingLastActiveAddress() {
        // Arrange - Only one active address exists
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(address));
        when(addressRepository.findByOwner(ownerId, ownerType)).thenReturn(List.of(address));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> deleteAddressUseCase.execute(ownerId, addressId, ownerType));
        
        assertEquals("Não é possível remover o último endereço de um utilizador.", exception.getMessage());
        verify(addressRepository, never()).delete(any());
        verify(validateOwnerUseCase).execute(ownerId, ownerType);
    }

    @Test
    void shouldAllowDeletionWhenMultipleActiveAddressesExist() {
        // Arrange
        UUID address2Id = UUID.randomUUID();
        Addresses address2 = new Addresses(
            address2Id,
            "Second Street",
            "789",
            "Apt 3",
            "Bela Vista",
            "São Paulo",
            "SP",
            "01311000"
        );

        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(address));
        when(addressRepository.findByOwner(ownerId, ownerType)).thenReturn(List.of(address, address2));

        // Act
        deleteAddressUseCase.execute(ownerId, addressId, ownerType);

        // Assert
        verify(addressRepository).delete(addressId);
        verify(validateOwnerUseCase).execute(ownerId, ownerType);
    }

    @Test
    void shouldNotConsiderInactiveAddressesInActiveCount() {
        // Arrange - Only one active address exists (the one being deleted)
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(address));
        when(addressRepository.findByOwner(ownerId, ownerType)).thenReturn(List.of(address));

        // Act & Assert - Should throw because it's the last active address
        BusinessException exception = assertThrows(BusinessException.class,
            () -> deleteAddressUseCase.execute(ownerId, addressId, ownerType));
        
        assertEquals("Não é possível remover o último endereço de um utilizador.", exception.getMessage());
        verify(addressRepository, never()).delete(any());
        verify(validateOwnerUseCase).execute(ownerId, ownerType);
    }

    @Test
    void shouldHandleDifferentOwnerTypes() {
        // Arrange
        String restaurantOwnerType = "RESTAURANT";
        UUID anotherAddressId = UUID.randomUUID();
        
        // Create a new address for the restaurant owner type
        Addresses restaurantAddress = new Addresses(
            addressId,
            "Restaurant Street",
            "101",
            "Loja 1",
            "Jardins",
            "São Paulo",
            "SP",
            "01415001"
        );
        
        Addresses anotherAddress = new Addresses(
            anotherAddressId,
            "Another Restaurant Street",
            "202",
            "Loja 2",
            "Itaim Bibi",
            "São Paulo",
            "SP",
            "04538001"
        );

        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(restaurantAddress));
        when(addressRepository.findByOwner(ownerId, restaurantOwnerType))
            .thenReturn(List.of(restaurantAddress, anotherAddress));

        // Act
        deleteAddressUseCase.execute(ownerId, addressId, restaurantOwnerType);

        // Assert
        verify(addressRepository).delete(addressId);
        verify(validateOwnerUseCase).execute(ownerId, restaurantOwnerType);
    }

    @Test
    void shouldNotAllowDeletionOfDifferentOwnerAddress() {
        // Arrange - The repository returns empty for findByIdAndOwnerId when the owner doesn't match
        when(addressRepository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.empty());

        // Act & Assert - Should throw AddressNotFoundException when address not found for owner
        assertThrows(AddressNotFoundException.class,
            () -> deleteAddressUseCase.execute(ownerId, addressId, ownerType));
        
        verify(addressRepository, never()).delete(any());
        verify(validateOwnerUseCase).execute(ownerId, ownerType);
    }
}
