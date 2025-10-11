package com.fiap.foodfiapp.core.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    void testNotFoundException() {
        NotFoundException exception = new NotFoundException("Not found");
        assertEquals("Not found", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testFileStorageException() {
        FileStorageException exception = new FileStorageException("Storage error");
        assertEquals("Storage error", exception.getMessage());
        
        FileStorageException exceptionWithCause = new FileStorageException("Storage error", new RuntimeException("cause"));
        assertEquals("Storage error", exceptionWithCause.getMessage());
        assertNotNull(exceptionWithCause.getCause());
    }

    @Test
    void testBusinessException() {
        BusinessException exception = new BusinessException("Business error");
        assertEquals("Business error", exception.getMessage());
    }

    @Test
    void testInvalidDataException() {
        InvalidDataException exception = new InvalidDataException("Invalid data");
        assertEquals("Invalid data", exception.getMessage());
    }

    @Test
    void testInvalidEmailException() {
        InvalidEmailException exception = new InvalidEmailException("Invalid email");
        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    void testInvalidPasswordException() {
        InvalidPasswordException exception = new InvalidPasswordException("Invalid password");
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void testInvalidCpfException() {
        InvalidCpfException exception = new InvalidCpfException("Invalid CPF");
        assertEquals("Invalid CPF", exception.getMessage());
    }

    @Test
    void testEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("Email exists");
        assertTrue(exception.getMessage().contains("Email exists"));
    }

    @Test
    void testLoginAlreadyExistsException() {
        LoginAlreadyExistsException exception = new LoginAlreadyExistsException("Login exists");
        assertTrue(exception.getMessage().contains("Login exists"));
    }

    @Test
    void testCpfAlreadyExistsException() {
        CpfAlreadyExistsException exception = new CpfAlreadyExistsException("CPF exists");
        assertTrue(exception.getMessage().contains("CPF exists"));
    }

    @Test
    void testUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUserTypeNotFoundException() {
        UserTypeNotFoundException exception = new UserTypeNotFoundException("UserType not found");
        assertEquals("UserType not found", exception.getMessage());
    }

    @Test
    void testUserTypeNameAlreadyExistsException() {
        UserTypeNameAlreadyExistsException exception = new UserTypeNameAlreadyExistsException("UserType name exists");
        assertTrue(exception.getMessage().contains("UserType name exists"));
    }

    @Test
    void testUserTypeInUseException() {
        UserTypeInUseException exception = new UserTypeInUseException();
        assertNotNull(exception);
    }

    @Test
    void testCoreUserTypeModificationException() {
        CoreUserTypeModificationException exception = new CoreUserTypeModificationException();
        assertNotNull(exception);
    }

    @Test
    void testRestaurantNotFoundException() {
        RestaurantNotFoundException exception = new RestaurantNotFoundException("id", "123");
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("123"));
    }

    @Test
    void testMenuItemNotFoundException() {
        MenuItemNotFoundException exception = new MenuItemNotFoundException("id", "123");
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("123"));
    }

    @Test
    void testAddressNotFoundException() {
        AddressNotFoundException exception = new AddressNotFoundException("Address not found");
        assertEquals("Address not found", exception.getMessage());
    }

    @Test
    void testUnauthorizedException() {
        UnauthorizedException exception = new UnauthorizedException("Unauthorized");
        assertEquals("Unauthorized", exception.getMessage());
    }

    @Test
    void testUnauthorizedAccessException() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException("Unauthorized access");
        assertEquals("Unauthorized access", exception.getMessage());
    }
}
