package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.InvalidCpfException;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidEmailException;
import com.fiap.foodfiapp.core.domain.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    @DisplayName("Should validate a valid user successfully")
    void shouldValidateValidUser() {
        User user = new User();
        user.setName("João Silva");
        user.setEmail("joao.silva@example.com");
        user.setCpf("12345678909");
        user.setLogin("joao.silva");

        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    // Name Tests
    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> UserValidator.validateName(null));
        assertEquals("User name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> UserValidator.validateName(""));
        assertEquals("User name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> UserValidator.validateName("   "));
        assertEquals("User name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is too short")
    void shouldThrowExceptionWhenNameIsTooShort() {
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> UserValidator.validateName("AB"));
        assertEquals("User name must be at least 3 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name exceeds max length")
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        String longName = "A".repeat(101);
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> UserValidator.validateName(longName));
        assertEquals("User name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name has no letters")
    void shouldThrowExceptionWhenNameHasNoLetters() {
        InvalidNameException exception = assertThrows(InvalidNameException.class,
                () -> UserValidator.validateName("123"));
        assertEquals("User name must contain at least one letter", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid name")
    void shouldValidateValidName() {
        assertDoesNotThrow(() -> UserValidator.validateName("João Silva"));
    }

    // Email Tests
    @Test
    @DisplayName("Should throw exception when email is null")
    void shouldThrowExceptionWhenEmailIsNull() {
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail(null));
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email is empty")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail(""));
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email is blank")
    void shouldThrowExceptionWhenEmailIsBlank() {
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail("   "));
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email has invalid format - no @")
    void shouldThrowExceptionWhenEmailHasInvalidFormatNoAt() {
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail("invalid.email.com"));
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email has invalid format - no domain")
    void shouldThrowExceptionWhenEmailHasInvalidFormatNoDomain() {
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail("invalid@"));
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email has invalid format - no extension")
    void shouldThrowExceptionWhenEmailHasInvalidFormatNoExtension() {
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail("invalid@domain"));
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when email exceeds max length")
    void shouldThrowExceptionWhenEmailExceedsMaxLength() {
        String longEmail = "a".repeat(90) + "@domain.com";
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> UserValidator.validateEmail(longEmail));
        assertEquals("Email cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid email")
    void shouldValidateValidEmail() {
        assertDoesNotThrow(() -> UserValidator.validateEmail("user@example.com"));
    }

    @Test
    @DisplayName("Should validate valid email with plus sign")
    void shouldValidateValidEmailWithPlusSign() {
        assertDoesNotThrow(() -> UserValidator.validateEmail("user+tag@example.com"));
    }

    @Test
    @DisplayName("Should validate valid email with dots")
    void shouldValidateValidEmailWithDots() {
        assertDoesNotThrow(() -> UserValidator.validateEmail("user.name@example.com"));
    }

    // CPF Tests
    @Test
    @DisplayName("Should throw exception when CPF is null")
    void shouldThrowExceptionWhenCpfIsNull() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf(null));
        assertEquals("CPF is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF is empty")
    void shouldThrowExceptionWhenCpfIsEmpty() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf(""));
        assertEquals("CPF is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF is blank")
    void shouldThrowExceptionWhenCpfIsBlank() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf("   "));
        assertEquals("CPF is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF has less than 11 digits")
    void shouldThrowExceptionWhenCpfHasLessThan11Digits() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf("1234567890"));
        assertEquals("CPF must contain exactly 11 digits", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF has more than 11 digits")
    void shouldThrowExceptionWhenCpfHasMoreThan11Digits() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf("123456789012"));
        assertEquals("CPF must contain exactly 11 digits", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF has letters")
    void shouldThrowExceptionWhenCpfHasLetters() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf("1234567890A"));
        assertEquals("CPF must contain exactly 11 digits", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF has all same digits")
    void shouldThrowExceptionWhenCpfHasAllSameDigits() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf("11111111111"));
        assertEquals("Invalid CPF format", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF has invalid algorithm")
    void shouldThrowExceptionWhenCpfHasInvalidAlgorithm() {
        InvalidCpfException exception = assertThrows(InvalidCpfException.class,
                () -> UserValidator.validateCpf("12345678901"));
        assertEquals("Invalid CPF", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid CPF without formatting")
    void shouldValidateValidCpfWithoutFormatting() {
        assertDoesNotThrow(() -> UserValidator.validateCpf("12345678909"));
    }

    @Test
    @DisplayName("Should validate valid CPF with dots and hyphen")
    void shouldValidateValidCpfWithDotsAndHyphen() {
        assertDoesNotThrow(() -> UserValidator.validateCpf("123.456.789-09"));
    }

    @Test
    @DisplayName("Should validate another valid CPF")
    void shouldValidateAnotherValidCpf() {
        assertDoesNotThrow(() -> UserValidator.validateCpf("11144477735"));
    }

    // Login Tests
    @Test
    @DisplayName("Should throw exception when login is null")
    void shouldThrowExceptionWhenLoginIsNull() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin(null));
        assertEquals("Login is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when login is empty")
    void shouldThrowExceptionWhenLoginIsEmpty() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin(""));
        assertEquals("Login is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when login is blank")
    void shouldThrowExceptionWhenLoginIsBlank() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin("   "));
        assertEquals("Login is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when login is too short")
    void shouldThrowExceptionWhenLoginIsTooShort() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin("ab"));
        assertEquals("Login must be at least 3 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when login exceeds max length")
    void shouldThrowExceptionWhenLoginExceedsMaxLength() {
        String longLogin = "a".repeat(51);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin(longLogin));
        assertEquals("Login cannot exceed 50 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when login has special characters")
    void shouldThrowExceptionWhenLoginHasSpecialCharacters() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin("user@login"));
        assertEquals("Login can only contain letters, numbers, dots, and underscores", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when login has spaces")
    void shouldThrowExceptionWhenLoginHasSpaces() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> UserValidator.validateLogin("user login"));
        assertEquals("Login can only contain letters, numbers, dots, and underscores", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid login")
    void shouldValidateValidLogin() {
        assertDoesNotThrow(() -> UserValidator.validateLogin("user123"));
    }

    @Test
    @DisplayName("Should validate valid login with dots")
    void shouldValidateValidLoginWithDots() {
        assertDoesNotThrow(() -> UserValidator.validateLogin("user.name"));
    }

    @Test
    @DisplayName("Should validate valid login with underscores")
    void shouldValidateValidLoginWithUnderscores() {
        assertDoesNotThrow(() -> UserValidator.validateLogin("user_name"));
    }
}
