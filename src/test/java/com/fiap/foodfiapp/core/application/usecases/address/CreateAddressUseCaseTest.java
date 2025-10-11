package com.fiap.foodfiapp.core.application.usecases.address;

import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.impl.CreateAddressesUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAddressUseCaseTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ValidateOwnerUseCase validateOwnerUseCase;

    @Captor
    private ArgumentCaptor<Addresses> addressCaptor;

    private CreateAddressesUseCaseImpl createAddressUseCase;

    private Addresses address;
    private UUID ownerId;
    private final String ownerType = AddressOwnerTypeEnum.USER.getDescription();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createAddressUseCase = new CreateAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);

        ownerId = UUID.randomUUID();
        
        // Create address using the constructor with all required fields
        address = new Addresses(
            UUID.randomUUID(), // ID will be set by the repository
            "Rua Teste",
            "123",
            "Apto 101",
            "Centro",
            "São Paulo",
            "SP",
            "01001000"
        );
        
        // Setup validateOwnerUseCase to do nothing by default
        doNothing().when(validateOwnerUseCase).execute(any(), any());
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        // Arrange
        Addresses savedAddress = new Addresses(
            UUID.randomUUID(),
            address.getPublicPlace(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getPostalCode()
        );

        when(addressRepository.save(any(Addresses.class), eq(ownerId), eq(ownerType)))
            .thenReturn(savedAddress);

        // Act
        Addresses result = createAddressUseCase.execute(address, ownerId, ownerType);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(address.getPublicPlace(), result.getPublicPlace());
        assertEquals(address.getNumber(), result.getNumber());
        assertEquals(address.getComplement(), result.getComplement());
        assertEquals(address.getNeighborhood(), result.getNeighborhood());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());
        assertEquals(address.getPostalCode(), result.getPostalCode());

        verify(addressRepository).save(addressCaptor.capture(), eq(ownerId), eq(ownerType));
        Addresses capturedAddress = addressCaptor.getValue();
        assertEquals(address.getPublicPlace(), capturedAddress.getPublicPlace());
        assertEquals(address.getNumber(), capturedAddress.getNumber());
    }

    @Test
    void shouldPassCorrectParametersToRepository() {
        // Arrange
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
            .thenReturn(address);

        // Act
        createAddressUseCase.execute(address, ownerId, ownerType);

        // Assert
        verify(addressRepository).save(eq(address), eq(ownerId), eq(ownerType));
    }

    @Test
    void shouldHandleNullComplement() {
        // Arrange
        address = new Addresses(
            UUID.randomUUID(),
            "Rua Teste",
            "123",
            null, // Null complement
            "Centro",
            "São Paulo",
            "SP",
            "01001000"
        );
        
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
            .thenReturn(address);

        // Act
        Addresses result = createAddressUseCase.execute(address, ownerId, ownerType);

        // Assert
        assertNull(result.getComplement());
        verify(addressRepository).save(any(Addresses.class), eq(ownerId), eq(ownerType));
    }

    @Test
    void shouldHandleNullComplementInConstructor() {
        // Arrange
        Addresses addressWithNullComplement = new Addresses(
            UUID.randomUUID(),
            "Rua Teste",
            "123",
            null, // Null complement
            "Centro",
            "São Paulo",
            "SP",
            "01001000"
        );
        
        when(addressRepository.save(any(Addresses.class), any(UUID.class), anyString()))
            .thenReturn(addressWithNullComplement);

        // Act
        Addresses result = createAddressUseCase.execute(addressWithNullComplement, ownerId, ownerType);

        // Assert
        assertNull(result.getComplement());
        verify(addressRepository).save(any(Addresses.class), eq(ownerId), eq(ownerType));
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> createAddressUseCase.execute(null, ownerId, ownerType));
        
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> createAddressUseCase.execute(address, null, ownerType));
        
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenOwnerTypeIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> createAddressUseCase.execute(address, ownerId, null));
        
        verify(addressRepository, never()).save(any(), any(), any());
    }

    @Test
    void shouldHandleEmptyOwnerType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> createAddressUseCase.execute(address, ownerId, ""));
        
        verify(addressRepository, never()).save(any(), any(), any());
    }
}
