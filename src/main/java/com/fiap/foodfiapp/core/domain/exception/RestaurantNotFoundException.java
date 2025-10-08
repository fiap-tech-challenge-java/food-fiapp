package com.fiap.foodfiapp.core.domain.exception;

public class RestaurantNotFoundException extends BusinessException {
    public RestaurantNotFoundException(String field, String value) {
        super("Restaurant not found with " + field + ": " + value);
    }
}
