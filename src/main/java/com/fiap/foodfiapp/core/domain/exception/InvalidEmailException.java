package com.fiap.foodfiapp.core.domain.exception;

public class InvalidEmailException extends BusinessException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException() {
        super("Invalid email format");
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
