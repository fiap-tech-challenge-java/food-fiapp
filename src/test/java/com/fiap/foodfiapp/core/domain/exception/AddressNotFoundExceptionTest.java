package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Address not found";

        AddressNotFoundException exception = new AddressNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFieldAndValue() {
        String fieldName = "id";
        Object fieldValue = "123";

        AddressNotFoundException exception = new AddressNotFoundException(fieldName, fieldValue);

        assertEquals("Address not found with id: '123'", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        AddressNotFoundException exception = new AddressNotFoundException();

        assertEquals("Address not found", exception.getMessage());
    }

    @Test
    void shouldExtendBusinessException() {
        AddressNotFoundException exception = new AddressNotFoundException("Test");

        assertTrue(exception instanceof BusinessException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldHandleNullFieldName() {
        AddressNotFoundException exception = new AddressNotFoundException(null, "value");

        assertEquals("Address not found with null: 'value'", exception.getMessage());
    }

    @Test
    void shouldHandleNullFieldValue() {
        AddressNotFoundException exception = new AddressNotFoundException("postalCode", null);

        assertEquals("Address not found with postalCode: 'null'", exception.getMessage());
    }
}
