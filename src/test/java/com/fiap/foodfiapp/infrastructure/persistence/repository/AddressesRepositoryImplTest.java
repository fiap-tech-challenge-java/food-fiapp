package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.AddressesSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressesRepositoryImplTest {

    @Mock
    private AddressesSpringDataRepository repository;

    @InjectMocks
    private AddressesRepositoryImpl addressesRepositoryImpl;

    private UUID addressId;
    private UUID ownerId;
    private String ownerType;
    private Addresses addresses;
    private AddressesEntity addressesEntity;

    @BeforeEach
    void setUp() {
        addressId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        ownerType = "USER";

        addresses = new Addresses(
            addressId,
            "Rua das Flores",
            "123",
            "Apto 45",
            "Centro",
            "São Paulo",
            "SP",
            "01234-567"
        );

        addressesEntity = AddressesEntity.builder()
            .id(addressId)
            .publicPlace("Rua das Flores")
            .number("123")
            .complement("Apto 45")
            .neighborhood("Centro")
            .city("São Paulo")
            .state("SP")
            .postalCode("01234-567")
            .ownerId(ownerId)
            .ownerType(ownerType)
            .build();
    }

    // ========== SAVE TESTS ==========

    @Test
    void shouldSaveAddressSuccessfully() {
        // Arrange
        when(repository.save(any(AddressesEntity.class))).thenReturn(addressesEntity);

        // Act
        Addresses result = addressesRepositoryImpl.save(addresses, ownerId, ownerType);

        // Assert
        assertNotNull(result);
        assertEquals(addressId, result.getId());
        assertEquals("Rua das Flores", result.getPublicPlace());
        assertEquals("123", result.getNumber());
        assertEquals("Apto 45", result.getComplement());

        verify(repository).save(any(AddressesEntity.class));

        // Verify that ownerId and ownerType were set on the entity
        verify(repository).save(argThat(entity ->
            entity.getOwnerId().equals(ownerId) &&
            entity.getOwnerType().equals(ownerType)
        ));
    }

    @Test
    void shouldSaveAddressWithDifferentOwnerType() {
        // Arrange
        String restaurantOwnerType = "RESTAURANT";
        AddressesEntity savedEntity = AddressesEntity.builder()
            .id(addressId)
            .publicPlace("Rua das Flores")
            .number("123")
            .ownerId(ownerId)
            .ownerType(restaurantOwnerType)
            .build();

        when(repository.save(any(AddressesEntity.class))).thenReturn(savedEntity);

        // Act
        Addresses result = addressesRepositoryImpl.save(addresses, ownerId, restaurantOwnerType);

        // Assert
        assertNotNull(result);
        verify(repository).save(argThat(entity ->
            entity.getOwnerType().equals(restaurantOwnerType)
        ));
    }

    @Test
    void shouldPropagateExceptionFromRepositoryOnSave() {
        // Arrange
        when(repository.save(any(AddressesEntity.class)))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class,
            () -> addressesRepositoryImpl.save(addresses, ownerId, ownerType));

        verify(repository).save(any(AddressesEntity.class));
    }

    // ========== FIND BY OWNER TESTS ==========

    @Test
    void shouldFindAddressesByOwnerSuccessfully() {
        // Arrange
        UUID secondAddressId = UUID.randomUUID();
        AddressesEntity secondEntity = AddressesEntity.builder()
            .id(secondAddressId)
            .publicPlace("Avenida Paulista")
            .number("1000")
            .ownerId(ownerId)
            .ownerType(ownerType)
            .build();

        List<AddressesEntity> entities = Arrays.asList(addressesEntity, secondEntity);

        when(repository.findByOwnerIdAndOwnerType(ownerId, ownerType)).thenReturn(entities);

        // Act
        List<Addresses> result = addressesRepositoryImpl.findByOwner(ownerId, ownerType);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Rua das Flores", result.get(0).getPublicPlace());
        assertEquals("Avenida Paulista", result.get(1).getPublicPlace());

        verify(repository).findByOwnerIdAndOwnerType(ownerId, ownerType);
    }

    @Test
    void shouldReturnEmptyListWhenNoAddressesFoundByOwner() {
        // Arrange
        when(repository.findByOwnerIdAndOwnerType(ownerId, ownerType))
            .thenReturn(Collections.emptyList());

        // Act
        List<Addresses> result = addressesRepositoryImpl.findByOwner(ownerId, ownerType);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findByOwnerIdAndOwnerType(ownerId, ownerType);
    }

    @Test
    void shouldHandleNullParametersInFindByOwner() {
        // Arrange
        when(repository.findByOwnerIdAndOwnerType(null, null))
            .thenReturn(Collections.emptyList());

        // Act
        List<Addresses> result = addressesRepositoryImpl.findByOwner(null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findByOwnerIdAndOwnerType(null, null);
    }

    @Test
    void shouldFindAddressesByDifferentOwnerTypes() {
        // Arrange
        String restaurantOwnerType = "RESTAURANT";
        List<AddressesEntity> entities = Arrays.asList(addressesEntity);

        when(repository.findByOwnerIdAndOwnerType(ownerId, restaurantOwnerType)).thenReturn(entities);

        // Act
        List<Addresses> result = addressesRepositoryImpl.findByOwner(ownerId, restaurantOwnerType);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(repository).findByOwnerIdAndOwnerType(ownerId, restaurantOwnerType);
    }

    // ========== DELETE TESTS ==========

    @Test
    void shouldDeleteAddressSuccessfully() {
        // Arrange
        doNothing().when(repository).deleteById(addressId);

        // Act
        addressesRepositoryImpl.delete(addressId);

        // Assert
        verify(repository).deleteById(addressId);
    }

    @Test
    void shouldHandleNullIdInDelete() {
        // Arrange
        doNothing().when(repository).deleteById(null);

        // Act
        addressesRepositoryImpl.delete(null);

        // Assert
        verify(repository).deleteById(null);
    }

    @Test
    void shouldPropagateExceptionFromRepositoryOnDelete() {
        // Arrange
        doThrow(new RuntimeException("Delete failed"))
            .when(repository).deleteById(addressId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> addressesRepositoryImpl.delete(addressId));
        verify(repository).deleteById(addressId);
    }

    // ========== FIND BY ID TESTS ==========

    @Test
    void shouldFindAddressByIdSuccessfully() {
        // Arrange
        when(repository.findById(addressId)).thenReturn(Optional.of(addressesEntity));

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findById(addressId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(addressId, result.get().getId());
        assertEquals("Rua das Flores", result.get().getPublicPlace());

        verify(repository).findById(addressId);
    }

    @Test
    void shouldReturnEmptyWhenAddressNotFoundById() {
        // Arrange
        when(repository.findById(addressId)).thenReturn(Optional.empty());

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findById(addressId);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findById(addressId);
    }

    @Test
    void shouldHandleNullIdInFindById() {
        // Arrange
        when(repository.findById(null)).thenReturn(Optional.empty());

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findById(null);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findById(null);
    }

    // ========== FIND BY ID AND OWNER ID TESTS ==========

    @Test
    void shouldFindAddressByIdAndOwnerIdSuccessfully() {
        // Arrange
        when(repository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(addressesEntity));

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findByIdAndOwnerId(addressId, ownerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(addressId, result.get().getId());
        assertEquals("Rua das Flores", result.get().getPublicPlace());

        verify(repository).findByIdAndOwnerId(addressId, ownerId);
    }

    @Test
    void shouldReturnEmptyWhenAddressNotFoundByIdAndOwnerId() {
        // Arrange
        when(repository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.empty());

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findByIdAndOwnerId(addressId, ownerId);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findByIdAndOwnerId(addressId, ownerId);
    }

    @Test
    void shouldHandleNullParametersInFindByIdAndOwnerId() {
        // Arrange
        when(repository.findByIdAndOwnerId(null, null)).thenReturn(Optional.empty());

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findByIdAndOwnerId(null, null);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findByIdAndOwnerId(null, null);
    }

    @Test
    void shouldReturnEmptyWhenOwnerIdDoesNotMatch() {
        // Test scenario where address exists but owner doesn't match
        // Arrange
        UUID differentOwnerId = UUID.randomUUID();
        when(repository.findByIdAndOwnerId(addressId, differentOwnerId)).thenReturn(Optional.empty());

        // Act
        Optional<Addresses> result = addressesRepositoryImpl.findByIdAndOwnerId(addressId, differentOwnerId);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findByIdAndOwnerId(addressId, differentOwnerId);
    }

    // ========== INTEGRATION AND EDGE CASE TESTS ==========

    @Test
    void shouldHandleMultipleOperationsInSequence() {
        // Arrange
        when(repository.save(any(AddressesEntity.class))).thenReturn(addressesEntity);
        when(repository.findById(addressId)).thenReturn(Optional.of(addressesEntity));
        when(repository.findByIdAndOwnerId(addressId, ownerId)).thenReturn(Optional.of(addressesEntity));
        doNothing().when(repository).deleteById(addressId);

        // Act & Assert
        // Save
        Addresses savedAddress = addressesRepositoryImpl.save(addresses, ownerId, ownerType);
        assertNotNull(savedAddress);

        // Find by ID
        Optional<Addresses> foundById = addressesRepositoryImpl.findById(addressId);
        assertTrue(foundById.isPresent());

        // Find by ID and Owner ID
        Optional<Addresses> foundByIdAndOwner = addressesRepositoryImpl.findByIdAndOwnerId(addressId, ownerId);
        assertTrue(foundByIdAndOwner.isPresent());

        // Delete
        assertDoesNotThrow(() -> addressesRepositoryImpl.delete(addressId));

        // Verify all interactions
        verify(repository).save(any(AddressesEntity.class));
        verify(repository).findById(addressId);
        verify(repository).findByIdAndOwnerId(addressId, ownerId);
        verify(repository).deleteById(addressId);
    }

    @Test
    void shouldVerifyOwnerIdAndOwnerTypeAreSetCorrectly() {
        // This test specifically verifies that the save method correctly sets the ownerId and ownerType

        // Arrange
        when(repository.save(any(AddressesEntity.class))).thenReturn(addressesEntity);

        // Act
        addressesRepositoryImpl.save(addresses, ownerId, "RESTAURANT");

        // Assert
        verify(repository).save(argThat(entity -> {
            assertEquals(ownerId, entity.getOwnerId());
            assertEquals("RESTAURANT", entity.getOwnerType());
            return true;
        }));
    }

    @Test
    void shouldHandleEmptyStringOwnerType() {
        // Arrange
        when(repository.save(any(AddressesEntity.class))).thenReturn(addressesEntity);

        // Act
        Addresses result = addressesRepositoryImpl.save(addresses, ownerId, "");

        // Assert
        assertNotNull(result);
        verify(repository).save(argThat(entity -> "".equals(entity.getOwnerType())));
    }

    @Test
    void shouldHandleAddressWithoutOptionalFields() {
        // Test address without complement

        // Arrange
        Addresses simpleAddress = new Addresses(
            UUID.randomUUID(),
            "Rua Simples",
            "100",
            null, // no complement
            "Vila Nova",
            "Rio de Janeiro",
            "RJ",
            "20000-000"
        );

        AddressesEntity simpleEntity = AddressesEntity.builder()
            .id(simpleAddress.getId())
            .publicPlace("Rua Simples")
            .number("100")
            .complement(null)
            .neighborhood("Vila Nova")
            .city("Rio de Janeiro")
            .state("RJ")
            .postalCode("20000-000")
            .ownerId(ownerId)
            .ownerType(ownerType)
            .build();

        when(repository.save(any(AddressesEntity.class))).thenReturn(simpleEntity);

        // Act
        Addresses result = addressesRepositoryImpl.save(simpleAddress, ownerId, ownerType);

        // Assert
        assertNotNull(result);
        assertEquals("Rua Simples", result.getPublicPlace());
        assertNull(result.getComplement());
        assertEquals("Vila Nova", result.getNeighborhood());

        verify(repository).save(any(AddressesEntity.class));
    }

    @Test
    void shouldHandleEmptyCollectionsCorrectly() {
        // Test behavior with empty collections

        // Arrange
        when(repository.findByOwnerIdAndOwnerType(any(UUID.class), anyString()))
            .thenReturn(Collections.emptyList());

        // Act
        List<Addresses> result = addressesRepositoryImpl.findByOwner(UUID.randomUUID(), "UNKNOWN");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
