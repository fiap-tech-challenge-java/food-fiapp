package com.fiap.foodfiapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeEntity extends BaseEntity {
    @Id
    // UUID generation is intentionally disabled; manual assignment is required for compatibility with legacy systems.
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String name;
}
