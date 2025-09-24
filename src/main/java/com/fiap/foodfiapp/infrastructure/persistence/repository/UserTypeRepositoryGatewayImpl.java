package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypeEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypePersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserTypeSpringDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserTypeRepositoryGatewayImpl implements UserTypeRepositoryGateway {
    private final UserTypeSpringDataRepository userTypeSpringDataRepository;

    public UserTypeRepositoryGatewayImpl(UserTypeSpringDataRepository userTypeSpringDataRepository) {
        this.userTypeSpringDataRepository = userTypeSpringDataRepository;
    }

    @Override
    public UserType save(UserType userType) {
        // Check if this is a create or update operation
        if (userType.getUuid() != null && userTypeSpringDataRepository.existsById(userType.getUuid())) {
            // Update existing entity
            UserTypeEntity existing = userTypeSpringDataRepository.findById(userType.getUuid())
                    .orElseThrow(() -> new RuntimeException("Entity not found"));
            UserTypePersistenceMapper.updateEntityFromDomain(existing, userType);
            UserTypeEntity saved = userTypeSpringDataRepository.save(existing);
            return UserTypePersistenceMapper.toDomain(saved);
        } else {
            // Create new entity
            // Check if entity exists to determine create vs update
            boolean exists = userType.getUuid() != null && userTypeSpringDataRepository.existsById(userType.getUuid());

            if (exists) {
                // Update: fetch existing entity and update its fields
                UserTypeEntity existingEntity = userTypeSpringDataRepository.findById(userType.getUuid())
                        .orElseThrow(() -> new RuntimeException("Entity not found for update"));
                UserTypePersistenceMapper.updateEntityFromDomain(existingEntity, userType);
                UserTypeEntity saved = userTypeSpringDataRepository.save(existingEntity);
                return UserTypePersistenceMapper.toDomain(saved);
            } else {
                // Create: create new entity
                UserTypeEntity newEntity = UserTypePersistenceMapper.toEntity(userType);
                UserTypeEntity saved = userTypeSpringDataRepository.save(newEntity);
                return UserTypePersistenceMapper.toDomain(saved);
            }
        }
    }

    @Override
    public Optional<UserType> findById(UUID id) {
        return userTypeSpringDataRepository.findById(id)
                .map(UserTypePersistenceMapper::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        return userTypeSpringDataRepository.findByName(name)
                .map(UserTypePersistenceMapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return userTypeSpringDataRepository.findAll().stream()
                .map(UserTypePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userTypeSpringDataRepository.deleteById(id);
    }
}
