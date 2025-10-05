package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserRepositoryAdapter implements UserRepository {
    private final UserSpringDataRepository springDataRepository;

    public UserRepositoryAdapter(UserSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = springDataRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByCpf(String cpf) {
        return springDataRepository.findByCpf(cpf).map(this::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return springDataRepository.findByLogin(login).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserTypeUuid(UUID userTypeUuid) {
        return springDataRepository.existsByUserType_Uuid(userTypeUuid);
    }

    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .cpf(user.getCpf())
                .email(user.getEmail())
                .login(user.getLogin())
                .password(user.getPassword())
                .userType(null)
                .build();
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getCpf(),
                null,
                null,
                entity.getIsActive(),
                null,
                null,
                entity.getPassword()
        );
    }
}
