package com.fiap.foodfiapp.core.domain.exception;

public class InvalidNameException extends BusinessException {
    public InvalidNameException(String message) {
        super(message);
    }

    public InvalidNameException() {
        super("Name must be between 3 and 100 characters");
    }
}

