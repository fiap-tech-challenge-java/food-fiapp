package com.fiap.foodfiapp.core.domain.exception;

public class UserTypeNotFoundException extends BusinessException {
    public UserTypeNotFoundException(String message) {
        super(message);
    }

    public UserTypeNotFoundException(String fieldName, Object fieldValue) {
        super(String.format("UserType not found with %s: '%s'", fieldName, fieldValue));
    }

    public UserTypeNotFoundException() {
        super("UserType not found");
    }

    public UserTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
