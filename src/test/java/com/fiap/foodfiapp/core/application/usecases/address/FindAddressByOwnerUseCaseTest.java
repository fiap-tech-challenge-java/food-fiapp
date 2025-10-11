package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.application.usecases.addresses.impl.FindAddressesByOwnerUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindAddressByOwnerUseCaseTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private FindAddressesByOwnerUseCaseImpl findAddressByOwnerUseCase;

    private UUID ownerId;
    private String ownerType;
    private Addresses address1;
    private Addresses address2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        ownerId = UUID.randomUUID();
        ownerType = "USER";
        
        // Setup test addresses using the constructor with all required parameters
        address1 = new Addresses(
            UUID.randomUUID(),
            "Rua A",
            "123",
            "Apt 101",
            "Centro",
            "São Paulo",
            "SP",
            "01001000"
        );

        address2 = new Addresses(
            UUID.randomUUID(),
            "Rua B",
            "456",
            null, // complement
            "Jardins",
            "São Paulo",
            "SP",
            "01414001"
        );
        
        // Set owner ID and type if needed by the test
        List<Addresses> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);
        when(addressRepository.findByOwner(any(), any())).thenReturn(addresses);
        
        // Note: ownerId and ownerType are now set when retrieving addresses from the repository
    }

    @Test
    void shouldReturnListOfAddressesForOwner() {
        // Arrange
        List<Addresses> expectedAddresses = Arrays.asList(address1, address2);
        when(addressRepository.findByOwner(ownerId, ownerType)).thenReturn(expectedAddresses);

        // Act
        List<Addresses> result = findAddressByOwnerUseCase.execute(ownerId, ownerType);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expectedAddresses));
        verify(addressRepository).findByOwner(ownerId, ownerType);
    }

    @Test
    void shouldReturnEmptyListWhenNoAddressesFound() {
        // Arrange
        when(addressRepository.findByOwner(any(), any())).thenReturn(Collections.emptyList());

        // Act
        List<Addresses> result = findAddressByOwnerUseCase.execute(ownerId, "RESTAURANT");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressRepository).findByOwner(ownerId, "RESTAURANT");
    }

    @Test
    void shouldHandleNullOwnerId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findAddressByOwnerUseCase.execute(null, ownerType));
        
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldHandleNullOwnerType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findAddressByOwnerUseCase.execute(ownerId, null));
        
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldHandleEmptyOwnerType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> findAddressByOwnerUseCase.execute(ownerId, ""));
        
        verify(addressRepository, never()).findByOwner(any(), any());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        when(addressRepository.findByOwner(ownerId, ownerType))
            .thenReturn(Collections.singletonList(address1));

        // Act
        List<Addresses> result = findAddressByOwnerUseCase.execute(ownerId, ownerType);

        // Assert
        assertThrows(UnsupportedOperationException.class, 
            () -> result.add(new Addresses()));
    }

    @Test
    void shouldReturnOnlyAddressesForSpecifiedOwnerAndType() {
        // Arrange
        UUID otherOwnerId = UUID.randomUUID();
        String otherOwnerType = "RESTAURANT";
        
        Addresses otherOwnerAddress = new Addresses(
            UUID.randomUUID(),
            "Other Owner Street",
            "789",
            "Apt 3",
            "Moema",
            "São Paulo",
            "SP",
            "04077001"
        );
        
        when(addressRepository.findByOwner(ownerId, ownerType))
            .thenReturn(Collections.singletonList(address1));
            
        when(addressRepository.findByOwner(otherOwnerId, otherOwnerType))
            .thenReturn(Collections.singletonList(otherOwnerAddress));

        // Act
        List<Addresses> result1 = findAddressByOwnerUseCase.execute(ownerId, ownerType);
        List<Addresses> result2 = findAddressByOwnerUseCase.execute(otherOwnerId, otherOwnerType);

        // Assert
        assertEquals(1, result1.size());
        assertEquals(address1, result1.get(0));
        
        assertEquals(1, result2.size());
        assertEquals(otherOwnerAddress, result2.get(0));
        
        verify(addressRepository).findByOwner(ownerId, ownerType);
        verify(addressRepository).findByOwner(otherOwnerId, otherOwnerType);
    }

    @Test
    void shouldHandleCaseSensitiveOwnerType() {
        // Arrange
        String mixedCaseType = "UsEr";
        when(addressRepository.findByOwner(ownerId, mixedCaseType))
            .thenReturn(Collections.singletonList(address1));

        // Act
        List<Addresses> result = findAddressByOwnerUseCase.execute(ownerId, mixedCaseType);

        // Assert
        assertEquals(1, result.size());
        assertEquals(address1, result.get(0));
        verify(addressRepository).findByOwner(ownerId, mixedCaseType);
    }
}
