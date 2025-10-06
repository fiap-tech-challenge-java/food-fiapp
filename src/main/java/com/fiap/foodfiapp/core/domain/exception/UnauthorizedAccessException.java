package com.fiap.foodfiapp.core.domain.exception;

public class UnauthorizedAccessException extends BusinessException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException() {
        super("Access denied. You are not authorized to perform this action");
    }
}
