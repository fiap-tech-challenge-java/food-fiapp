package com.fiap.foodfiapp.core.domain.exception;

public class UserTypeInUseException extends BusinessException {

    public UserTypeInUseException() {
        super("Cannot update or delete a user type that is already in use.");
    }
}

