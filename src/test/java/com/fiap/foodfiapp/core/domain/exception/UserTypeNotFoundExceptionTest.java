package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTypeNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "UserType not found";

        UserTypeNotFoundException exception = new UserTypeNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFieldAndValue() {
        String fieldName = "id";
        Object fieldValue = "123";

        UserTypeNotFoundException exception = new UserTypeNotFoundException(fieldName, fieldValue);

        assertEquals("UserType not found with id: '123'", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException();

        assertEquals("UserType not found", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullFieldName() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException(null, "value");

        assertEquals("UserType not found with null: 'value'", exception.getMessage());
    }

    @Test
    void shouldHandleNullFieldValue() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException("name", null);

        assertEquals("name", exception.getMessage());
    }
}
