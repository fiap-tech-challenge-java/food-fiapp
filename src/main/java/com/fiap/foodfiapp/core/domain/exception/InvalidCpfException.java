package com.fiap.foodfiapp.core.domain.exception;

public class InvalidCpfException extends BusinessException {
    public InvalidCpfException(String message) {
        super(message);
    }

    public InvalidCpfException() {
        super("Invalid CPF format");
    }

    public InvalidCpfException(String message, Throwable cause) {
        super(message, cause);
    }
}
