package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidPostalCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressValidatorTest {

    @Test
    @DisplayName("Should validate a valid address successfully")
    void shouldValidateValidAddress() {
        Addresses address = new Addresses();
        address.setPublicPlace("Rua das Flores");
        address.setNumber("123");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setPostalCode("01234567");

        assertDoesNotThrow(() -> AddressValidator.validate(address));
    }

    // Public Place Tests
    @Test
    @DisplayName("Should throw exception when public place is null")
    void shouldThrowExceptionWhenPublicPlaceIsNull() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validatePublicPlace(null));
        assertEquals("Public place (street, avenue, etc.) is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when public place is empty")
    void shouldThrowExceptionWhenPublicPlaceIsEmpty() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validatePublicPlace(""));
        assertEquals("Public place (street, avenue, etc.) is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when public place is blank")
    void shouldThrowExceptionWhenPublicPlaceIsBlank() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validatePublicPlace("   "));
        assertEquals("Public place (street, avenue, etc.) is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when public place is too short")
    void shouldThrowExceptionWhenPublicPlaceIsTooShort() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validatePublicPlace("AB"));
        assertEquals("Public place must be at least 3 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when public place exceeds max length")
    void shouldThrowExceptionWhenPublicPlaceExceedsMaxLength() {
        String longPublicPlace = "A".repeat(201);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validatePublicPlace(longPublicPlace));
        assertEquals("Public place cannot exceed 200 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid public place")
    void shouldValidateValidPublicPlace() {
        assertDoesNotThrow(() -> AddressValidator.validatePublicPlace("Rua das Flores"));
    }

    // Number Tests
    @Test
    @DisplayName("Should throw exception when number is null")
    void shouldThrowExceptionWhenNumberIsNull() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNumber(null));
        assertEquals("Address number is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when number is empty")
    void shouldThrowExceptionWhenNumberIsEmpty() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNumber(""));
        assertEquals("Address number is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when number is blank")
    void shouldThrowExceptionWhenNumberIsBlank() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNumber("   "));
        assertEquals("Address number is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when number exceeds max length")
    void shouldThrowExceptionWhenNumberExceedsMaxLength() {
        String longNumber = "1".repeat(21);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNumber(longNumber));
        assertEquals("Address number cannot exceed 20 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid number")
    void shouldValidateValidNumber() {
        assertDoesNotThrow(() -> AddressValidator.validateNumber("123"));
    }

    // Neighborhood Tests
    @Test
    @DisplayName("Should throw exception when neighborhood is null")
    void shouldThrowExceptionWhenNeighborhoodIsNull() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNeighborhood(null));
        assertEquals("Neighborhood is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when neighborhood is empty")
    void shouldThrowExceptionWhenNeighborhoodIsEmpty() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNeighborhood(""));
        assertEquals("Neighborhood is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when neighborhood is blank")
    void shouldThrowExceptionWhenNeighborhoodIsBlank() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNeighborhood("   "));
        assertEquals("Neighborhood is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when neighborhood is too short")
    void shouldThrowExceptionWhenNeighborhoodIsTooShort() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNeighborhood("A"));
        assertEquals("Neighborhood must be at least 2 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when neighborhood exceeds max length")
    void shouldThrowExceptionWhenNeighborhoodExceedsMaxLength() {
        String longNeighborhood = "A".repeat(101);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateNeighborhood(longNeighborhood));
        assertEquals("Neighborhood cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid neighborhood")
    void shouldValidateValidNeighborhood() {
        assertDoesNotThrow(() -> AddressValidator.validateNeighborhood("Centro"));
    }

    // City Tests
    @Test
    @DisplayName("Should throw exception when city is null")
    void shouldThrowExceptionWhenCityIsNull() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateCity(null));
        assertEquals("City is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when city is empty")
    void shouldThrowExceptionWhenCityIsEmpty() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateCity(""));
        assertEquals("City is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when city is blank")
    void shouldThrowExceptionWhenCityIsBlank() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateCity("   "));
        assertEquals("City is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when city is too short")
    void shouldThrowExceptionWhenCityIsTooShort() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateCity("A"));
        assertEquals("City must be at least 2 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when city exceeds max length")
    void shouldThrowExceptionWhenCityExceedsMaxLength() {
        String longCity = "A".repeat(101);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateCity(longCity));
        assertEquals("City cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid city")
    void shouldValidateValidCity() {
        assertDoesNotThrow(() -> AddressValidator.validateCity("São Paulo"));
    }

    // State Tests
    @Test
    @DisplayName("Should throw exception when state is null")
    void shouldThrowExceptionWhenStateIsNull() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateState(null));
        assertEquals("State is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when state is empty")
    void shouldThrowExceptionWhenStateIsEmpty() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateState(""));
        assertEquals("State is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when state is blank")
    void shouldThrowExceptionWhenStateIsBlank() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateState("   "));
        assertEquals("State is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when state is too short")
    void shouldThrowExceptionWhenStateIsTooShort() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateState("A"));
        assertEquals("State must be at least 2 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when state exceeds max length")
    void shouldThrowExceptionWhenStateExceedsMaxLength() {
        String longState = "A".repeat(51);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateState(longState));
        assertEquals("State cannot exceed 50 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid state")
    void shouldValidateValidState() {
        assertDoesNotThrow(() -> AddressValidator.validateState("SP"));
    }

    // Postal Code Tests
    @Test
    @DisplayName("Should throw exception when postal code is null")
    void shouldThrowExceptionWhenPostalCodeIsNull() {
        InvalidPostalCodeException exception = assertThrows(InvalidPostalCodeException.class,
                () -> AddressValidator.validatePostalCode(null));
        assertEquals("Postal code is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when postal code is empty")
    void shouldThrowExceptionWhenPostalCodeIsEmpty() {
        InvalidPostalCodeException exception = assertThrows(InvalidPostalCodeException.class,
                () -> AddressValidator.validatePostalCode(""));
        assertEquals("Postal code is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when postal code is blank")
    void shouldThrowExceptionWhenPostalCodeIsBlank() {
        InvalidPostalCodeException exception = assertThrows(InvalidPostalCodeException.class,
                () -> AddressValidator.validatePostalCode("   "));
        assertEquals("Postal code is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when postal code has invalid format")
    void shouldThrowExceptionWhenPostalCodeHasInvalidFormat() {
        InvalidPostalCodeException exception = assertThrows(InvalidPostalCodeException.class,
                () -> AddressValidator.validatePostalCode("1234567"));
        assertEquals("Postal code must contain exactly 8 digits", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when postal code has letters")
    void shouldThrowExceptionWhenPostalCodeHasLetters() {
        InvalidPostalCodeException exception = assertThrows(InvalidPostalCodeException.class,
                () -> AddressValidator.validatePostalCode("1234567A"));
        assertEquals("Postal code must contain exactly 8 digits", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid postal code without formatting")
    void shouldValidateValidPostalCodeWithoutFormatting() {
        assertDoesNotThrow(() -> AddressValidator.validatePostalCode("01234567"));
    }

    @Test
    @DisplayName("Should validate valid postal code with hyphen")
    void shouldValidateValidPostalCodeWithHyphen() {
        assertDoesNotThrow(() -> AddressValidator.validatePostalCode("01234-567"));
    }

    @Test
    @DisplayName("Should validate valid postal code with spaces")
    void shouldValidateValidPostalCodeWithSpaces() {
        assertDoesNotThrow(() -> AddressValidator.validatePostalCode("01234 567"));
    }

    // Complement Tests
    @Test
    @DisplayName("Should throw exception when complement exceeds max length")
    void shouldThrowExceptionWhenComplementExceedsMaxLength() {
        String longComplement = "A".repeat(201);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> AddressValidator.validateComplement(longComplement));
        assertEquals("Address complement cannot exceed 200 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate null complement")
    void shouldValidateNullComplement() {
        assertDoesNotThrow(() -> AddressValidator.validateComplement(null));
    }

    @Test
    @DisplayName("Should validate valid complement")
    void shouldValidateValidComplement() {
        assertDoesNotThrow(() -> AddressValidator.validateComplement("Apto 101"));
    }
}
