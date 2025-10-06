package com.fiap.foodfiapp.core.domain.exception;

public class LoginAlreadyExistsException extends BusinessException {
    public LoginAlreadyExistsException(String login) {
        super(String.format("Login '%s' is already registered", login));
    }

    public LoginAlreadyExistsException() {
        super("Login is already registered");
    }

    public LoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
