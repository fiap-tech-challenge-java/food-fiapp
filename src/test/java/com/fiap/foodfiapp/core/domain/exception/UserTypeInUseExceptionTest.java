package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTypeInUseExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        UserTypeInUseException exception = new UserTypeInUseException();

        assertEquals("Cannot update or delete a user type that is already in use.", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        UserTypeInUseException exception = new UserTypeInUseException();

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
