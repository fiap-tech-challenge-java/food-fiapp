package com.fiap.foodfiapp.core.domain.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super(String.format("Email '%s' is already registered", email));
    }

    public EmailAlreadyExistsException() {
        super("Email is already registered");
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
