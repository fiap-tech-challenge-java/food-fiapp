package com.fiap.foodfiapp.core.domain.enums;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AddressOwnerTypeEnumTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        // Act
        AddressOwnerTypeEnum[] values = AddressOwnerTypeEnum.values();
        
        // Assert
        assertEquals(2, values.length);
        assertTrue(Arrays.asList(values).contains(AddressOwnerTypeEnum.USER));
        assertTrue(Arrays.asList(values).contains(AddressOwnerTypeEnum.RESTAURANT));
    }

    @Test
    void shouldReturnCorrectDescriptionForUser() {
        // Arrange
        AddressOwnerTypeEnum userType = AddressOwnerTypeEnum.USER;
        
        // Act & Assert
        assertEquals("USER", userType.getDescription());
    }

    @Test
    void shouldReturnCorrectDescriptionForRestaurant() {
        // Arrange
        AddressOwnerTypeEnum restaurantType = AddressOwnerTypeEnum.RESTAURANT;
        
        // Act & Assert
        assertEquals("RESTAURANT", restaurantType.getDescription());
    }

    @Test
    void shouldBeAbleToGetEnumFromString() {
        // Act
        AddressOwnerTypeEnum userType = AddressOwnerTypeEnum.valueOf("USER");
        AddressOwnerTypeEnum restaurantType = AddressOwnerTypeEnum.valueOf("RESTAURANT");
        
        // Assert
        assertEquals(AddressOwnerTypeEnum.USER, userType);
        assertEquals(AddressOwnerTypeEnum.RESTAURANT, restaurantType);
    }

    @Test
    void shouldThrowExceptionForInvalidEnumValue() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> AddressOwnerTypeEnum.valueOf("INVALID"));
    }

    @Test
    void shouldHaveUniqueDescriptions() {
        // Arrange
        String[] descriptions = Arrays.stream(AddressOwnerTypeEnum.values())
            .map(AddressOwnerTypeEnum::getDescription)
            .toArray(String[]::new);
        
        // Assert
        assertEquals(descriptions.length, Arrays.stream(descriptions).distinct().count(),
            "Each enum value should have a unique description");
    }

    @Test
    void shouldHaveNonNullDescriptions() {
        // Act & Assert
        for (AddressOwnerTypeEnum type : AddressOwnerTypeEnum.values()) {
            assertNotNull(type.getDescription(), 
                "Description for " + type.name() + " should not be null");
            assertFalse(type.getDescription().isBlank(), 
                "Description for " + type.name() + " should not be blank");
        }
    }

    @Test
    void shouldHaveConsistentToString() {
        // Act & Assert
        for (AddressOwnerTypeEnum type : AddressOwnerTypeEnum.values()) {
            assertEquals(type.name(), type.toString(), 
                "toString() should return the same as name() for " + type.name());
        }
    }

    @Test
    void shouldBeAbleToUseInSwitchStatement() {
        // This test verifies that the enum can be used in a switch statement without errors
        boolean userFound = false;
        boolean restaurantFound = false;
        
        for (AddressOwnerTypeEnum type : AddressOwnerTypeEnum.values()) {
            switch (type) {
                case USER:
                    userFound = true;
                    break;
                case RESTAURANT:
                    restaurantFound = true;
                    break;
                // No default needed as all cases are covered
            }
        }
        
        assertTrue(userFound, "USER case should be handled in switch");
        assertTrue(restaurantFound, "RESTAURANT case should be handled in switch");
    }
}
