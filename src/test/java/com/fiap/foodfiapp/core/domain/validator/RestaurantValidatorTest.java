package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.InvalidCuisineTypeException;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidOpeningHoursException;
import com.fiap.foodfiapp.core.domain.exception.InvalidRestaurantNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantValidatorTest {

    @Test
    @DisplayName("Should validate a valid restaurant successfully")
    void shouldValidateValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Bella Italia");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("Mon-Fri: 11:00-22:00");
        restaurant.setDescription("Authentic Italian restaurant");

        assertDoesNotThrow(() -> RestaurantValidator.validate(restaurant));
    }

    // Name Tests
    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName(null));
        assertEquals("Restaurant name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName(""));
        assertEquals("Restaurant name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName("   "));
        assertEquals("Restaurant name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is too short")
    void shouldThrowExceptionWhenNameIsTooShort() {
        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName("AB"));
        assertEquals("Restaurant name must be at least 3 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name exceeds max length")
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        String longName = "A".repeat(101);
        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName(longName));
        assertEquals("Restaurant name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name has no letters")
    void shouldThrowExceptionWhenNameHasNoLetters() {
        InvalidRestaurantNameException exception = assertThrows(InvalidRestaurantNameException.class,
                () -> RestaurantValidator.validateName("123"));
        assertEquals("Restaurant name must contain at least one letter", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid name")
    void shouldValidateValidName() {
        assertDoesNotThrow(() -> RestaurantValidator.validateName("Bella Italia"));
    }

    // Cuisine Type Tests
    @Test
    @DisplayName("Should throw exception when cuisine type is null")
    void shouldThrowExceptionWhenCuisineTypeIsNull() {
        InvalidCuisineTypeException exception = assertThrows(InvalidCuisineTypeException.class,
                () -> RestaurantValidator.validateCuisineType(null));
        assertEquals("Cuisine type is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cuisine type is empty")
    void shouldThrowExceptionWhenCuisineTypeIsEmpty() {
        InvalidCuisineTypeException exception = assertThrows(InvalidCuisineTypeException.class,
                () -> RestaurantValidator.validateCuisineType(""));
        assertEquals("Cuisine type is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cuisine type is blank")
    void shouldThrowExceptionWhenCuisineTypeIsBlank() {
        InvalidCuisineTypeException exception = assertThrows(InvalidCuisineTypeException.class,
                () -> RestaurantValidator.validateCuisineType("   "));
        assertEquals("Cuisine type is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cuisine type is too short")
    void shouldThrowExceptionWhenCuisineTypeIsTooShort() {
        InvalidCuisineTypeException exception = assertThrows(InvalidCuisineTypeException.class,
                () -> RestaurantValidator.validateCuisineType("AB"));
        assertEquals("Cuisine type must be at least 3 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cuisine type exceeds max length")
    void shouldThrowExceptionWhenCuisineTypeExceedsMaxLength() {
        String longCuisineType = "A".repeat(51);
        InvalidCuisineTypeException exception = assertThrows(InvalidCuisineTypeException.class,
                () -> RestaurantValidator.validateCuisineType(longCuisineType));
        assertEquals("Cuisine type cannot exceed 50 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when cuisine type has no letters")
    void shouldThrowExceptionWhenCuisineTypeHasNoLetters() {
        InvalidCuisineTypeException exception = assertThrows(InvalidCuisineTypeException.class,
                () -> RestaurantValidator.validateCuisineType("123"));
        assertEquals("Cuisine type must contain at least one letter", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid cuisine type")
    void shouldValidateValidCuisineType() {
        assertDoesNotThrow(() -> RestaurantValidator.validateCuisineType("Italian"));
    }

    // Opening Hours Tests
    @Test
    @DisplayName("Should throw exception when opening hours is null")
    void shouldThrowExceptionWhenOpeningHoursIsNull() {
        InvalidOpeningHoursException exception = assertThrows(InvalidOpeningHoursException.class,
                () -> RestaurantValidator.validateOpeningHours(null));
        assertEquals("Opening hours are required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when opening hours is empty")
    void shouldThrowExceptionWhenOpeningHoursIsEmpty() {
        InvalidOpeningHoursException exception = assertThrows(InvalidOpeningHoursException.class,
                () -> RestaurantValidator.validateOpeningHours(""));
        assertEquals("Opening hours are required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when opening hours is blank")
    void shouldThrowExceptionWhenOpeningHoursIsBlank() {
        InvalidOpeningHoursException exception = assertThrows(InvalidOpeningHoursException.class,
                () -> RestaurantValidator.validateOpeningHours("   "));
        assertEquals("Opening hours are required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when opening hours exceeds max length")
    void shouldThrowExceptionWhenOpeningHoursExceedsMaxLength() {
        String longOpeningHours = "A".repeat(201);
        InvalidOpeningHoursException exception = assertThrows(InvalidOpeningHoursException.class,
                () -> RestaurantValidator.validateOpeningHours(longOpeningHours));
        assertEquals("Opening hours cannot exceed 200 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate valid opening hours")
    void shouldValidateValidOpeningHours() {
        assertDoesNotThrow(() -> RestaurantValidator.validateOpeningHours("Mon-Fri: 11:00-22:00"));
    }

    @Test
    @DisplayName("Should validate opening hours at max length")
    void shouldValidateOpeningHoursAtMaxLength() {
        String maxOpeningHours = "A".repeat(200);
        assertDoesNotThrow(() -> RestaurantValidator.validateOpeningHours(maxOpeningHours));
    }

    // Description Tests
    @Test
    @DisplayName("Should throw exception when description exceeds max length")
    void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
        String longDescription = "A".repeat(501);
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> RestaurantValidator.validateDescription(longDescription));
        assertEquals("Restaurant description cannot exceed 500 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate null description")
    void shouldValidateNullDescription() {
        assertDoesNotThrow(() -> RestaurantValidator.validateDescription(null));
    }

    @Test
    @DisplayName("Should validate empty description")
    void shouldValidateEmptyDescription() {
        assertDoesNotThrow(() -> RestaurantValidator.validateDescription(""));
    }

    @Test
    @DisplayName("Should validate valid description")
    void shouldValidateValidDescription() {
        assertDoesNotThrow(() -> RestaurantValidator.validateDescription("Authentic Italian restaurant with traditional recipes"));
    }

    @Test
    @DisplayName("Should validate description at max length")
    void shouldValidateDescriptionAtMaxLength() {
        String maxDescription = "A".repeat(500);
        assertDoesNotThrow(() -> RestaurantValidator.validateDescription(maxDescription));
    }
}
