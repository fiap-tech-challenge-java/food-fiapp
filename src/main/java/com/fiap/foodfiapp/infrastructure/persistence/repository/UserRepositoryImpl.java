package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserSpringDataRepository userSpringDataRepository;
    private final UserPersistenceMapper userMapper = UserPersistenceMapper.INSTANCE;

    @Override
    public User save(User user) {
        var userEntity = userMapper.toEntity(user);
        var savedEntity = userSpringDataRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userSpringDataRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userSpringDataRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByCpf(String cpf) {
        return userSpringDataRepository.findByCpf(cpf).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userSpringDataRepository.findByLogin(login).map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userSpringDataRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userSpringDataRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserTypeUuid(UUID userTypeUuid) {
        return userSpringDataRepository.existsByUserType_Uuid(userTypeUuid);
    }
}