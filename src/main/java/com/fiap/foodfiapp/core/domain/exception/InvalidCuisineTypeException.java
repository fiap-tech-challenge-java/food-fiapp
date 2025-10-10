package com.fiap.foodfiapp.core.domain.exception;

public class InvalidCuisineTypeException extends BusinessException {
    public InvalidCuisineTypeException(String message) {
        super(message);
    }

    public InvalidCuisineTypeException() {
        super("Cuisine type must be between 3 and 50 characters");
    }
}

