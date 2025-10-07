package com.fiap.foodfiapp.core.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    private static class TestEntity extends BaseEntity {
    }

    @Test
    void shouldCreateBaseEntityWithDefaultValues() {
        TestEntity entity = new TestEntity();

        assertTrue(entity.getIsActive());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        TestEntity entity = new TestEntity();
        OffsetDateTime now = OffsetDateTime.now();

        entity.setCreatedAt(now);

        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        TestEntity entity = new TestEntity();
        OffsetDateTime now = OffsetDateTime.now();

        entity.setUpdatedAt(now);

        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetIsActive() {
        TestEntity entity = new TestEntity();

        entity.setIsActive(false);

        assertFalse(entity.getIsActive());
    }

    @Test
    void shouldHandleNullIsActive() {
        TestEntity entity = new TestEntity();

        entity.setIsActive(null);

        assertNull(entity.getIsActive());
    }
}
