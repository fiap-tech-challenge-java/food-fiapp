package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidPostalCodeException;

public class AddressValidator {

    public static void validate(Addresses address) {
        validatePublicPlace(address.getPublicPlace());
        validateNumber(address.getNumber());
        validateNeighborhood(address.getNeighborhood());
        validateCity(address.getCity());
        validateState(address.getState());
        validatePostalCode(address.getPostalCode());
    }

    public static void validatePublicPlace(String publicPlace) {
        if (publicPlace == null || publicPlace.trim().isEmpty()) {
            throw new InvalidDataException("Public place (street, avenue, etc.) is required");
        }

        if (publicPlace.trim().length() < 3) {
            throw new InvalidDataException("Public place must be at least 3 characters long");
        }

        if (publicPlace.length() > 200) {
            throw new InvalidDataException("Public place cannot exceed 200 characters");
        }
    }

    public static void validateNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new InvalidDataException("Address number is required");
        }

        if (number.length() > 20) {
            throw new InvalidDataException("Address number cannot exceed 20 characters");
        }
    }

    public static void validateNeighborhood(String neighborhood) {
        if (neighborhood == null || neighborhood.trim().isEmpty()) {
            throw new InvalidDataException("Neighborhood is required");
        }

        if (neighborhood.trim().length() < 2) {
            throw new InvalidDataException("Neighborhood must be at least 2 characters long");
        }

        if (neighborhood.length() > 100) {
            throw new InvalidDataException("Neighborhood cannot exceed 100 characters");
        }
    }

    public static void validateCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new InvalidDataException("City is required");
        }

        if (city.trim().length() < 2) {
            throw new InvalidDataException("City must be at least 2 characters long");
        }

        if (city.length() > 100) {
            throw new InvalidDataException("City cannot exceed 100 characters");
        }
    }

    public static void validateState(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new InvalidDataException("State is required");
        }

        if (state.trim().length() < 2) {
            throw new InvalidDataException("State must be at least 2 characters long");
        }

        if (state.length() > 50) {
            throw new InvalidDataException("State cannot exceed 50 characters");
        }
    }

    public static void validatePostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new InvalidPostalCodeException("Postal code is required");
        }

        // Remove common formatting characters
        String cleanPostalCode = postalCode.replaceAll("[\\s\\-]", "");

        // Brazilian postal code (CEP) should have 8 digits
        if (!cleanPostalCode.matches("\\d{8}")) {
            throw new InvalidPostalCodeException("Postal code must contain exactly 8 digits");
        }
    }

    public static void validateComplement(String complement) {
        // Complement is optional, but if provided, it should have max length
        if (complement != null && complement.length() > 200) {
            throw new InvalidDataException("Address complement cannot exceed 200 characters");
        }
    }
}
