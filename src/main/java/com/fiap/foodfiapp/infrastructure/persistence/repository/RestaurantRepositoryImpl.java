package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.RestaurantSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantSpringDataRepository restaurantSpringDataRepository;
    private final RestaurantPersistenceMapper restaurantMapper = RestaurantPersistenceMapper.INSTANCE;

    @Override
    public Restaurant save(Restaurant restaurant) {
        var entity = restaurantMapper.toEntity(restaurant);
        var savedEntity = restaurantSpringDataRepository.save(entity);
        return restaurantMapper.toDomain(savedEntity);
    }

    @Override
    public Restaurant findById(UUID id) {
        return restaurantSpringDataRepository.findByIdAndActiveTrue(id)
                .map(restaurantMapper::toDomain)
                .orElse(null);
    }

    @Override
    public boolean existsById(UUID id) {
        return restaurantSpringDataRepository.existsById(id);
    }

    @Override
    public Restaurant findByNameAndUser(String name, UUID userId) {
        return restaurantSpringDataRepository.findByUserOwnerIdAndNameAndActiveTrue(userId, name)
                .map(restaurantMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Restaurant changeOwner(UUID restaurantId, UUID newOwnerId) {
        return null;
    }

    @Override
    public List<Restaurant> findAllByUserId(UUID userId) {
        return restaurantSpringDataRepository.findAllByUserOwnerIdAndActiveTrue(userId)
                .orElse(Collections.emptyList())
                .stream()
                .map(restaurantMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        restaurantSpringDataRepository.findById(id).ifPresent(entity -> {
            entity.setIsActive(false);
            restaurantSpringDataRepository.save(entity);
        });
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        var entity = restaurantMapper.toEntity(restaurant);
        var savedEntity = restaurantSpringDataRepository.save(entity);
        return restaurantMapper.toDomain(savedEntity);
    }
}