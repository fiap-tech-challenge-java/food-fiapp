package com.fiap.foodfiapp.infrastructure.persistence.entity;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTypePersistenceMapperTest {

    @Test
    void toDomain_shouldMapAllFieldsIncludingTimestamps() {
        // Arrange
        var entity = UserTypeEntity.builder()
                .uuid(UUID.randomUUID())
                .name("CUSTOMER")
                .build();
        var createdAt = OffsetDateTime.now().minusDays(1);
        var updatedAt = OffsetDateTime.now();
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        // Act
        UserType domain = UserTypePersistenceMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getUuid(), domain.getUuid());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(createdAt, domain.getCreatedAt());
        assertEquals(updatedAt, domain.getUpdatedAt());
    }
}

