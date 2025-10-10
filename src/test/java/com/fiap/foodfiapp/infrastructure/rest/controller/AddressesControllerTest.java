package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.addresses.CreateAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.DeleteAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.UpdateAddressesUseCase;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.AddressesResponse;
import com.fiap.foodfiapp.model.CreateAddressesRequest;
import com.fiap.foodfiapp.model.UpdateAddressesRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressesControllerTest {

    @Mock
    private CreateAddressesUseCase createAddressesUseCase;

    @Mock
    private UpdateAddressesUseCase updateAddressesUseCase;

    @Mock
    private DeleteAddressesUseCase deleteAddressesUseCase;

    @Mock
    private FindAddressesByOwnerUseCase findAddressesByOwnerUseCase;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AddressesController addressesController;

    private UUID userId;
    private UUID addressId;
    private CreateAddressesRequest createRequest;
    private UpdateAddressesRequest updateRequest;
    private Addresses address;
    private AddressesResponse addressResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        createRequest = new CreateAddressesRequest();
        createRequest.setPublicPlace("Rua das Flores");
        createRequest.setNumber("123");
        createRequest.setComplement("Apto 45");
        createRequest.setNeighborhood("Centro");
        createRequest.setCity("São Paulo");
        createRequest.setState("SP");
        createRequest.setPostalCode("01234-567");

        updateRequest = new UpdateAddressesRequest();
        updateRequest.setPublicPlace("Rua das Rosas");
        updateRequest.setNumber("456");
        updateRequest.setComplement("Casa");
        updateRequest.setNeighborhood("Vila Nova");
        updateRequest.setCity("Rio de Janeiro");
        updateRequest.setState("RJ");
        updateRequest.setPostalCode("20123-456");

        // Using the correct constructor for Addresses entity
        address = new Addresses(
            addressId,
            "Rua das Flores",
            "123",
            "Apto 45",
            "Centro",
            "São Paulo",
            "SP",
            "01234-567"
        );

        addressResponse = new AddressesResponse();
        addressResponse.setId(addressId);
        addressResponse.setPublicPlace("Rua das Flores");
        addressResponse.setNumber("123");
        addressResponse.setComplement("Apto 45");
        addressResponse.setNeighborhood("Centro");
        addressResponse.setCity("São Paulo");
        addressResponse.setState("SP");
        addressResponse.setPostalCode("01234-567");
    }

    @Test
    void shouldCreateAddressForUserSuccessfully() {
        // Arrange
        when(createAddressesUseCase.execute(any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(address);

        // Act
        ResponseEntity<AddressesResponse> response = addressesController.createAddressesForUser(userId, createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(addressId, response.getBody().getId());
        assertEquals("Rua das Flores", response.getBody().getPublicPlace());
        assertEquals("123", response.getBody().getNumber());
        assertEquals("São Paulo", response.getBody().getCity());

        verify(createAddressesUseCase).execute(any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldListAddressesByUserIdSuccessfully() {
        // Arrange
        List<Addresses> addresses = Arrays.asList(address);
        when(findAddressesByOwnerUseCase.execute(userId, AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        ResponseEntity<List<AddressesResponse>> response = addressesController.listAddressesByUserId(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(addressId, response.getBody().get(0).getId());
        assertEquals("Rua das Flores", response.getBody().get(0).getPublicPlace());

        verify(findAddressesByOwnerUseCase).execute(userId, AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldListAddressesReturnEmptyListWhenNoAddressesFound() {
        // Arrange
        when(findAddressesByOwnerUseCase.execute(userId, AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<AddressesResponse>> response = addressesController.listAddressesByUserId(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(findAddressesByOwnerUseCase).execute(userId, AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldUpdateAddressForUserSuccessfully() {
        // Arrange
        Addresses updatedAddress = new Addresses(
            addressId,
            "Rua das Rosas",
            "456",
            "Casa",
            "Vila Nova",
            "Rio de Janeiro",
            "RJ",
            "20123-456"
        );

        when(updateAddressesUseCase.execute(eq(addressId), any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(updatedAddress);

        // Act
        ResponseEntity<AddressesResponse> response = addressesController.updateAddressesForUser(userId, addressId, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(addressId, response.getBody().getId());
        assertEquals("Rua das Rosas", response.getBody().getPublicPlace());
        assertEquals("456", response.getBody().getNumber());
        assertEquals("Rio de Janeiro", response.getBody().getCity());

        verify(updateAddressesUseCase).execute(eq(addressId), any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldDeleteAddressForUserSuccessfully() {
        // Arrange
        doNothing().when(deleteAddressesUseCase).execute(userId, addressId, AddressOwnerTypeEnum.USER.getDescription());

        // Act
        ResponseEntity<Void> response = addressesController.deleteAddressesForUser(userId, addressId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(deleteAddressesUseCase).execute(userId, addressId, AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldHandleCreateAddressWithNullFields() {
        // Arrange
        CreateAddressesRequest requestWithNulls = new CreateAddressesRequest();
        requestWithNulls.setPublicPlace("Rua Test");
        requestWithNulls.setNumber("1");
        requestWithNulls.setCity("Test City");
        requestWithNulls.setState("TS");
        requestWithNulls.setPostalCode("12345-678");
        requestWithNulls.setNeighborhood("Test Neighborhood");
        // complement is null

        Addresses addressWithNulls = new Addresses(
            addressId,
            "Rua Test",
            "1",
            null,
            "Test Neighborhood",
            "Test City",
            "TS",
            "12345-678"
        );

        when(createAddressesUseCase.execute(any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(addressWithNulls);

        // Act
        ResponseEntity<AddressesResponse> response = addressesController.createAddressesForUser(userId, requestWithNulls);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Rua Test", response.getBody().getPublicPlace());
        assertNull(response.getBody().getComplement());

        verify(createAddressesUseCase).execute(any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldHandleUpdateAddressWithPartialData() {
        // Arrange
        UpdateAddressesRequest partialUpdate = new UpdateAddressesRequest();
        partialUpdate.setPublicPlace("New Street");
        partialUpdate.setNumber("999");
        // other fields are null

        Addresses partiallyUpdatedAddress = new Addresses(
            addressId,
            "New Street",
            "999",
            "Apto 45", // keeping original
            "Centro", // keeping original
            "São Paulo", // keeping original
            "SP", // keeping original
            "01234-567" // keeping original
        );

        when(updateAddressesUseCase.execute(eq(addressId), any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(partiallyUpdatedAddress);

        // Act
        ResponseEntity<AddressesResponse> response = addressesController.updateAddressesForUser(userId, addressId, partialUpdate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Street", response.getBody().getPublicPlace());
        assertEquals("999", response.getBody().getNumber());

        verify(updateAddressesUseCase).execute(eq(addressId), any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription()));
    }

    @Test
    void shouldHandleMultipleAddressesInList() {
        // Arrange
        UUID secondAddressId = UUID.randomUUID();
        Addresses secondAddress = new Addresses(
            secondAddressId,
            "Rua das Palmeiras",
            "789",
            "Bloco B",
            "Jardins",
            "São Paulo",
            "SP",
            "01234-999"
        );

        List<Addresses> addresses = Arrays.asList(address, secondAddress);
        when(findAddressesByOwnerUseCase.execute(userId, AddressOwnerTypeEnum.USER.getDescription()))
            .thenReturn(addresses);

        // Act
        ResponseEntity<List<AddressesResponse>> response = addressesController.listAddressesByUserId(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Check first address
        assertEquals(addressId, response.getBody().get(0).getId());
        assertEquals("Rua das Flores", response.getBody().get(0).getPublicPlace());

        // Check second address
        assertEquals(secondAddressId, response.getBody().get(1).getId());
        assertEquals("Rua das Palmeiras", response.getBody().get(1).getPublicPlace());

        verify(findAddressesByOwnerUseCase).execute(userId, AddressOwnerTypeEnum.USER.getDescription());
    }

    @Test
    void shouldVerifyCorrectOwnerTypeIsPassedToUseCases() {
        // Arrange
        when(createAddressesUseCase.execute(any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(address);

        // Act
        addressesController.createAddressesForUser(userId, createRequest);

        // Assert - verify that USER owner type is always passed
        verify(createAddressesUseCase).execute(any(Addresses.class), eq(userId), eq("USER"));
    }

    @Test
    void shouldVerifyMapperIsCalledCorrectly() {
        // Arrange
        when(createAddressesUseCase.execute(any(Addresses.class), eq(userId), eq(AddressOwnerTypeEnum.USER.getDescription())))
            .thenReturn(address);

        // Act
        ResponseEntity<AddressesResponse> response = addressesController.createAddressesForUser(userId, createRequest);

        // Assert
        assertNotNull(response.getBody());
        // Verify that the mapper correctly converted the domain entity to response
        assertEquals(address.getId(), response.getBody().getId());
        assertEquals(address.getPublicPlace(), response.getBody().getPublicPlace());
        assertEquals(address.getNumber(), response.getBody().getNumber());
        assertEquals(address.getComplement(), response.getBody().getComplement());
        assertEquals(address.getNeighborhood(), response.getBody().getNeighborhood());
        assertEquals(address.getCity(), response.getBody().getCity());
        assertEquals(address.getState(), response.getBody().getState());
        assertEquals(address.getPostalCode(), response.getBody().getPostalCode());
    }
}
