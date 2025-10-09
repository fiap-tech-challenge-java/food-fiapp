package com.fiap.foodfiapp.core.domain.exception;

public class InvalidRestaurantNameException extends BusinessException {
    public InvalidRestaurantNameException(String message) {
        super(message);
    }

    public InvalidRestaurantNameException() {
        super("Restaurant name must be between 3 and 100 characters");
    }
}

