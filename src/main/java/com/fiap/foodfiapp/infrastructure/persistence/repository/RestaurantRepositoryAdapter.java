package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepository {
    private final RestaurantJpaRepository restaurantJpaRepository;

    @Override
    public boolean existsById(UUID id) {
        return restaurantJpaRepository.existsById(id);
    }
}
