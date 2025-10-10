package com.fiap.foodfiapp.core.domain.exception;

public class RestaurantNameAlreadyExistsException extends BusinessException {
    public RestaurantNameAlreadyExistsException(String name) {
        super(String.format("A restaurant with name '%s' already exists for this owner", name));
    }

    public RestaurantNameAlreadyExistsException() {
        super("A restaurant with this name already exists for this owner");
    }
}

