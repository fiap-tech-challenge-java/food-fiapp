package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "User not found";

        UserNotFoundException exception = new UserNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFieldAndValue() {
        String fieldName = "id";
        Object fieldValue = "123";

        UserNotFoundException exception = new UserNotFoundException(fieldName, fieldValue);

        assertEquals("User not found with id: '123'", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        UserNotFoundException exception = new UserNotFoundException();

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        UserNotFoundException exception = new UserNotFoundException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullFieldName() {
        UserNotFoundException exception = new UserNotFoundException(null, "value");

        assertEquals("User not found with null: 'value'", exception.getMessage());
    }

    @Test
    void shouldHandleNullFieldValue() {
        UserNotFoundException exception = new UserNotFoundException("email", null);

        assertEquals("email", exception.getMessage());
    }
}
