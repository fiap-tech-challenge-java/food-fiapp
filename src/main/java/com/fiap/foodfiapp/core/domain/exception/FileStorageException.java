package com.fiap.foodfiapp.core.domain.exception;

public class FileStorageException extends BusinessException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStorageException() {
        super("File storage operation failed");
    }
}
