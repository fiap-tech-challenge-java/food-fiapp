package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserRepositoryGatewayImpl implements UserRepositoryGateway {
    private final UserSpringDataRepository userSpringDataRepository;
    private static final UserPersistenceMapper USER_PERSISTENCE_MAPPER = UserPersistenceMapper.INSTANCE;

    public UserRepositoryGatewayImpl(UserSpringDataRepository userSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = USER_PERSISTENCE_MAPPER.mapToEntity(user);
        UserEntity saved = userSpringDataRepository.save(entity);

        return USER_PERSISTENCE_MAPPER.mapToUser(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userSpringDataRepository.findById(id)
                .map(USER_PERSISTENCE_MAPPER::mapToUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userSpringDataRepository.findByEmail(email)
                .map(USER_PERSISTENCE_MAPPER::mapToUser);
    }

    @Override
    public List<User> findAll() {
        return userSpringDataRepository.findAll().stream()
                .map(USER_PERSISTENCE_MAPPER::mapToUser)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userSpringDataRepository.deleteById(id);
    }
}

