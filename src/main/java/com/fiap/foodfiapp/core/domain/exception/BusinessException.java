package com.fiap.foodfiapp.core.domain.exception;

/**
 * BusinessException is a generic exception for business rule violations in the domain layer.
 * It should be thrown by use cases or domain services when a business rule is broken.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

