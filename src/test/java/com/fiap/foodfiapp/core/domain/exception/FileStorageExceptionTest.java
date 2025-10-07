package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileStorageExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Failed to store file";

        FileStorageException exception = new FileStorageException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "File upload failed";
        Throwable cause = new RuntimeException("Disk full");

        FileStorageException exception = new FileStorageException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        FileStorageException exception = new FileStorageException();

        assertEquals("File storage operation failed", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        FileStorageException exception = new FileStorageException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
