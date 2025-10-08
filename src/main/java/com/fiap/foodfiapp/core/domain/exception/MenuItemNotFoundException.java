package com.fiap.foodfiapp.core.domain.exception;

public class MenuItemNotFoundException extends BusinessException {
    public MenuItemNotFoundException(String field, String value) {
        super("MenuItem not found with " + field + ": " + value);
    }
}
