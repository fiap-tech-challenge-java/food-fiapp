package com.fiap.foodfiapp.core.domain.exception;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String fieldName, Object fieldValue) {
        super(String.format("User not found with %s: '%s'", fieldName, fieldValue));
    }

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
