package com.fiap.foodfiapp.core.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public abstract class BaseEntity {
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
