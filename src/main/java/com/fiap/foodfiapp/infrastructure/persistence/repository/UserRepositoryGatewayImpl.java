package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryGatewayImpl implements UserRepositoryGateway {
    private final UserSpringDataRepository userSpringDataRepository;

    public UserRepositoryGatewayImpl(UserSpringDataRepository userSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserPersistenceMapper.toEntity(user);
        UserEntity saved = userSpringDataRepository.save(entity);
        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userSpringDataRepository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userSpringDataRepository.findByEmail(email)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userSpringDataRepository.findAll().stream()
                .map(UserPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userSpringDataRepository.deleteById(id);
    }
}

