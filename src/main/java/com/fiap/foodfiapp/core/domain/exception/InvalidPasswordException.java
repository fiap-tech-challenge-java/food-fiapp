package com.fiap.foodfiapp.core.domain.exception;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException() {
        super("Password does not meet security requirements");
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
