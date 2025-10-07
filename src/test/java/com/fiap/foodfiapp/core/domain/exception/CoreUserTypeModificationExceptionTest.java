package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreUserTypeModificationExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        CoreUserTypeModificationException exception = new CoreUserTypeModificationException();

        assertEquals("Cannot modify or delete a core user type.", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        CoreUserTypeModificationException exception = new CoreUserTypeModificationException();

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }
}
