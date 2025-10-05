package com.fiap.foodfiapp.core.domain.exception;

public class UserTypeNameAlreadyExistsException extends BusinessException {

    public UserTypeNameAlreadyExistsException(String name) {
        super("User type with name '" + name + "' already exists.");
    }
}
