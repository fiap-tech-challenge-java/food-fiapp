package com.fiap.foodfiapp.core.domain.validator;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.InvalidCuisineTypeException;
import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import com.fiap.foodfiapp.core.domain.exception.InvalidOpeningHoursException;
import com.fiap.foodfiapp.core.domain.exception.InvalidRestaurantNameException;

public class RestaurantValidator {

    public static void validate(Restaurant restaurant) {
        validateName(restaurant.getName());
        validateCuisineType(restaurant.getCuisineType());
        validateOpeningHours(restaurant.getOpeningHours());
        validateDescription(restaurant.getDescription());
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRestaurantNameException("Restaurant name is required");
        }

        if (name.trim().length() < 3) {
            throw new InvalidRestaurantNameException("Restaurant name must be at least 3 characters long");
        }

        if (name.length() > 100) {
            throw new InvalidRestaurantNameException("Restaurant name cannot exceed 100 characters");
        }

        // Validate that name contains at least some letters (not just numbers or special chars)
        if (!name.matches(".*[a-zA-Z]+.*")) {
            throw new InvalidRestaurantNameException("Restaurant name must contain at least one letter");
        }
    }

    public static void validateCuisineType(String cuisineType) {
        if (cuisineType == null || cuisineType.trim().isEmpty()) {
            throw new InvalidCuisineTypeException("Cuisine type is required");
        }

        if (cuisineType.trim().length() < 3) {
            throw new InvalidCuisineTypeException("Cuisine type must be at least 3 characters long");
        }

        if (cuisineType.length() > 50) {
            throw new InvalidCuisineTypeException("Cuisine type cannot exceed 50 characters");
        }

        // Validate that cuisine type contains letters (not just numbers or special chars)
        if (!cuisineType.matches(".*[a-zA-Z]+.*")) {
            throw new InvalidCuisineTypeException("Cuisine type must contain at least one letter");
        }
    }

    public static void validateOpeningHours(String openingHours) {
        if (openingHours == null || openingHours.trim().isEmpty()) {
            throw new InvalidOpeningHoursException("Opening hours are required");
        }

        if (openingHours.length() > 200) {
            throw new InvalidOpeningHoursException("Opening hours cannot exceed 200 characters");
        }
    }

    public static void validateDescription(String description) {
        // Description is optional, but if provided, it should have a max length
        if (description != null && description.length() > 500) {
            throw new InvalidDataException("Restaurant description cannot exceed 500 characters");
        }
    }
}
