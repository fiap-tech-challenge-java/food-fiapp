package com.fiap.foodfiapp.core.domain.entity;

import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void shouldCreateValidAddress() {
        UUID id = UUID.randomUUID();

        Address address = new Address(id, "Rua Test", "123", "Apto 1", "Centro", "São Paulo", "SP", "01000-000");

        assertEquals(id, address.getId());
        assertEquals("Rua Test", address.getPublicPlace());
        assertEquals("123", address.getNumber());
        assertEquals("Apto 1", address.getComplement());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("01000-000", address.getPostalCode());
    }

    @Test
    void shouldCreateEmptyAddress() {
        Address address = new Address();

        assertNull(address.getId());
        assertNull(address.getPublicPlace());
        assertNull(address.getNumber());
        assertNull(address.getComplement());
        assertNull(address.getNeighborhood());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getPostalCode());
    }

    @Test
    void shouldThrowExceptionWhenPublicPlaceIsNull() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, null, "123", "Apto 1", "Centro", "São Paulo", "SP", "01000-000"));

        assertEquals("Public place (street, avenue, etc.) is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPublicPlaceIsEmpty() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "", "123", "Apto 1", "Centro", "São Paulo", "SP", "01000-000"));

        assertEquals("Public place (street, avenue, etc.) is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPublicPlaceIsBlank() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "   ", "123", "Apto 1", "Centro", "São Paulo", "SP", "01000-000"));

        assertEquals("Public place (street, avenue, etc.) is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNumberIsNull() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "Rua Test", null, "Apto 1", "Centro", "São Paulo", "SP", "01000-000"));

        assertEquals("Number is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNumberIsEmpty() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "Rua Test", "", "Apto 1", "Centro", "São Paulo", "SP", "01000-000"));

        assertEquals("Number is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNeighborhoodIsNull() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "Rua Test", "123", "Apto 1", null, "São Paulo", "SP", "01000-000"));

        assertEquals("Neighborhood is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCityIsNull() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "Rua Test", "123", "Apto 1", "Centro", null, "SP", "01000-000"));

        assertEquals("City is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStateIsNull() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "Rua Test", "123", "Apto 1", "Centro", "São Paulo", null, "01000-000"));

        assertEquals("State is mandatory.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPostalCodeIsNull() {
        UUID id = UUID.randomUUID();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
            new Address(id, "Rua Test", "123", "Apto 1", "Centro", "São Paulo", "SP", null));

        assertEquals("Postal code is mandatory.", exception.getMessage());
    }

    @Test
    void shouldSetAllPropertiesCorrectly() {
        Address address = new Address();
        UUID id = UUID.randomUUID();

        address.setId(id);
        address.setPublicPlace("Avenida Paulista");
        address.setNumber("1000");
        address.setComplement("Conjunto 1");
        address.setNeighborhood("Bela Vista");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setPostalCode("01310-100");

        assertEquals(id, address.getId());
        assertEquals("Avenida Paulista", address.getPublicPlace());
        assertEquals("1000", address.getNumber());
        assertEquals("Conjunto 1", address.getComplement());
        assertEquals("Bela Vista", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("01310-100", address.getPostalCode());
    }

    @Test
    void shouldAcceptNullComplement() {
        UUID id = UUID.randomUUID();

        Address address = new Address(id, "Rua Test", "123", null, "Centro", "São Paulo", "SP", "01000-000");

        assertNull(address.getComplement());
    }
}
