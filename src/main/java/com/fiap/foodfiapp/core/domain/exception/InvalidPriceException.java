package com.fiap.foodfiapp.core.domain.exception;

public class InvalidPriceException extends BusinessException {
    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException() {
        super("Price must be a positive value greater than zero");
    }
}

