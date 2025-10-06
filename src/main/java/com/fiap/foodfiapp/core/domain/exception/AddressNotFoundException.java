package com.fiap.foodfiapp.core.domain.exception;

public class AddressNotFoundException extends BusinessException {
    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(String fieldName, Object fieldValue) {
        super(String.format("Address not found with %s: '%s'", fieldName, fieldValue));
    }

    public AddressNotFoundException() {
        super("Address not found");
    }
}
