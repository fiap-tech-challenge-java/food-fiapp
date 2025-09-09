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
        UserTypeEntity entity = UserTypePersistenceMapper.toEntity(userType);
        UserTypeEntity saved = userTypeSpringDataRepository.save(entity);
        return UserTypePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<UserType> findById(UUID id) {
        return userTypeSpringDataRepository.findById(id)
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
