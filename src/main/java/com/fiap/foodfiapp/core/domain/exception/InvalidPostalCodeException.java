package com.fiap.foodfiapp.core.domain.exception;

public class InvalidPostalCodeException extends BusinessException {
    public InvalidPostalCodeException(String message) {
        super(message);
    }

    public InvalidPostalCodeException() {
        super("Invalid postal code format");
    }
}

