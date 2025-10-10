package com.fiap.foodfiapp.core.domain.exception;

public class InvalidMenuItemNameException extends BusinessException {
    public InvalidMenuItemNameException(String message) {
        super(message);
    }

    public InvalidMenuItemNameException() {
        super("Menu item name must be between 3 and 100 characters");
    }
}

