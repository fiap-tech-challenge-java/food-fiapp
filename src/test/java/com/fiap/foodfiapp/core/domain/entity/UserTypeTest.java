package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void shouldCreateEmptyUserType() {
        UserType userType = new UserType();

        assertNull(userType.getUuid());
        assertNull(userType.getName());
        assertFalse(userType.isActive());
    }

    @Test
    void shouldCreateUserTypeWithIdAndName() {
        UUID id = UUID.randomUUID();

        UserType userType = new UserType(id, "ADMIN");

        assertEquals(id, userType.getUuid());
        assertEquals("ADMIN", userType.getName());
        assertFalse(userType.isActive());
    }

    @Test
    void shouldCreateUserTypeWithTimestamps() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        UserType userType = new UserType(id, "CLIENT", createdAt, updatedAt);

        assertEquals(id, userType.getUuid());
        assertEquals("CLIENT", userType.getName());
        assertEquals(createdAt, userType.getCreatedAt());
        assertEquals(updatedAt, userType.getUpdatedAt());
        assertFalse(userType.isActive());
    }

    @Test
    void shouldCreateCompleteUserType() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        UserType userType = new UserType(id, "MANAGER", true, createdAt, updatedAt);

        assertEquals(id, userType.getUuid());
        assertEquals("MANAGER", userType.getName());
        assertTrue(userType.isActive());
        assertEquals(createdAt, userType.getCreatedAt());
        assertEquals(updatedAt, userType.getUpdatedAt());
    }

    @Test
    void shouldSetAllProperties() {
        UserType userType = new UserType();
        UUID id = UUID.randomUUID();

        userType.setUuid(id);
        userType.setName("EMPLOYEE");
        userType.setActive(true);

        assertEquals(id, userType.getUuid());
        assertEquals("EMPLOYEE", userType.getName());
        assertTrue(userType.isActive());
    }

    @Test
    void shouldHandleNullValues() {
        UserType userType = new UserType();

        userType.setUuid(null);
        userType.setName(null);
        userType.setActive(false);

        assertNull(userType.getUuid());
        assertNull(userType.getName());
        assertFalse(userType.isActive());
    }

    @Test
    void shouldInheritFromBaseEntity() {
        UserType userType = new UserType();
        OffsetDateTime now = OffsetDateTime.now();

        userType.setCreatedAt(now);
        userType.setUpdatedAt(now);
        userType.setIsActive(false);

        assertEquals(now, userType.getCreatedAt());
        assertEquals(now, userType.getUpdatedAt());
        assertFalse(userType.getIsActive());
    }

    @Test
    void shouldToggleActiveStatus() {
        UserType userType = new UserType();

        userType.setActive(true);
        assertTrue(userType.isActive());

        userType.setActive(false);
        assertFalse(userType.isActive());
    }
}
