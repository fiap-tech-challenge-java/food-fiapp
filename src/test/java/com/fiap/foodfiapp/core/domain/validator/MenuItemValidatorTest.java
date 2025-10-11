package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidMenuItemNameException;
import com.fiap.foodfiapp.core.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemValidatorTest {

    @Test
    @DisplayName("Should validate a valid menu item successfully")
    void shouldValidateValidMenuItem() {
        MenuItem menuItem = new MenuItem(
            java.util.UUID.randomUUID(),
            "Pizza Margherita",
            "Traditional Italian pizza",
            29.90,
            false,
            null,
            java.util.UUID.randomUUID(),
            java.time.OffsetDateTime.now(),
            java.time.OffsetDateTime.now()
        );

        assertDoesNotThrow(() -> MenuItemValidator.validate(menuItem));
    }

    // Name Tests
    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidMenuItemNameException exception = assertThrows(InvalidMenuItemNameException.class,
                () -> MenuItemValidator.validateName(null));
        assertEquals("Menu item name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        InvalidMenuItemNameException exception = assertThrows(InvalidMenuItemNameException.class,
                () -> MenuItemValidator.validateName(""));
        assertEquals("Menu item name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidMenuItemNameException exception = assertThrows(InvalidMenuItemNameException.class,
                () -> MenuItemValidator.validateName("   "));
        assertEquals("Menu item name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is too short")
    void shouldThrowExceptionWhenNameIsTooShort() {
        InvalidMenuItemNameException exception = assertThrows(InvalidMenuItemNameException.class,
                () -> MenuItemValidator.validateName("AB"));
        assertEquals("Menu item name must be at least 3 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name exceeds max length")
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        String longName = "A".repeat(101);
        InvalidMenuItemNameException exception = assertThrows(InvalidMenuItemNameException.class,
                () -> MenuItemValidator.validateName(longName));
        assertEquals("Menu item name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name has no letters")
    void shouldThrowExceptionWhenNameHasNoLetters() {
        InvalidMenuItemNameException exception = assertThrows(InvalidMenuItemNameException.class,
                () -> MenuItemValidator.validateName("123"));
        assertEquals("Menu item name must contain at least one letter", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid name")
    void shouldValidateValidName() {
        assertDoesNotThrow(() -> MenuItemValidator.validateName("Pizza Margherita"));
    }

    // Price Tests
    @Test
    @DisplayName("Should throw exception when price is null")
    void shouldThrowExceptionWhenPriceIsNull() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(null));
        assertEquals("Price is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when price is zero")
    void shouldThrowExceptionWhenPriceIsZero() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(0.0));
        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when price is negative")
    void shouldThrowExceptionWhenPriceIsNegative() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(-10.0));
        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when price exceeds maximum")
    void shouldThrowExceptionWhenPriceExceedsMaximum() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(1000000.0));
        assertEquals("Price cannot exceed R$ 999,999.99", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when price has more than 2 decimal places")
    void shouldThrowExceptionWhenPriceHasMoreThan2DecimalPlaces() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(29.999));
        assertEquals("Price cannot have more than 2 decimal places", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when price is NaN")
    void shouldThrowExceptionWhenPriceIsNaN() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(Double.NaN));
        assertEquals("Invalid price value", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when price is infinite")
    void shouldThrowExceptionWhenPriceIsInfinite() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                () -> MenuItemValidator.validatePrice(Double.POSITIVE_INFINITY));
        assertEquals("Price cannot exceed R$ 999,999.99", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid price with 2 decimal places")
    void shouldValidateValidPriceWith2DecimalPlaces() {
        assertDoesNotThrow(() -> MenuItemValidator.validatePrice(29.90));
    }

    @Test
    @DisplayName("Should validate valid price with 1 decimal place")
    void shouldValidateValidPriceWith1DecimalPlace() {
        assertDoesNotThrow(() -> MenuItemValidator.validatePrice(29.5));
    }

    @Test
    @DisplayName("Should validate valid price without decimal places")
    void shouldValidateValidPriceWithoutDecimalPlaces() {
        assertDoesNotThrow(() -> MenuItemValidator.validatePrice(29.0));
    }

    @Test
    @DisplayName("Should validate maximum valid price")
    void shouldValidateMaximumValidPrice() {
        assertDoesNotThrow(() -> MenuItemValidator.validatePrice(999999.99));
    }

    @Test
    @DisplayName("Should validate minimum valid price")
    void shouldValidateMinimumValidPrice() {
        assertDoesNotThrow(() -> MenuItemValidator.validatePrice(0.01));
    }

    // Description Tests
    @Test
    @DisplayName("Should throw exception when description exceeds max length")
    void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
        String longDescription = "A".repeat(501);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> MenuItemValidator.validateDescription(longDescription));
        assertEquals("Menu item description cannot exceed 500 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate null description")
    void shouldValidateNullDescription() {
        assertDoesNotThrow(() -> MenuItemValidator.validateDescription(null));
    }

    @Test
    @DisplayName("Should validate empty description")
    void shouldValidateEmptyDescription() {
        assertDoesNotThrow(() -> MenuItemValidator.validateDescription(""));
    }

    @Test
    @DisplayName("Should validate valid description")
    void shouldValidateValidDescription() {
        assertDoesNotThrow(() -> MenuItemValidator.validateDescription("Traditional Italian pizza with fresh mozzarella"));
    }

    @Test
    @DisplayName("Should validate description at max length")
    void shouldValidateDescriptionAtMaxLength() {
        String maxDescription = "A".repeat(500);
        assertDoesNotThrow(() -> MenuItemValidator.validateDescription(maxDescription));
    }
}
