package com.fiap.foodfiapp.core.domain.exception;

public class InvalidDataException extends BusinessException {
    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String field, String value) {
        super(String.format("Invalid data for field '%s': %s", field, value));
    }

    public InvalidDataException() {
        super("Invalid data provided");
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
