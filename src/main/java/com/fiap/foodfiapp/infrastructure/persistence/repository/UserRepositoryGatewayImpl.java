package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.CreateUser;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserRepositoryGatewayImpl implements UserRepositoryGateway {
    private final UserSpringDataRepository userSpringDataRepository;
    private final PasswordEncoder passwordEncoder;

    private static final UserPersistenceMapper USER_PERSISTENCE_MAPPER = UserPersistenceMapper.INSTANCE;

    public UserRepositoryGatewayImpl(UserSpringDataRepository userSpringDataRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userSpringDataRepository = userSpringDataRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User createUser(CreateUser createUser) {
        userSpringDataRepository.findByEmail(createUser.getEmail()).ifPresent(user -> {
            throw new BusinessException("User with this email already exists.");
        });

        UserEntity savedEntity = USER_PERSISTENCE_MAPPER.mapToEntity(createUser);

        savedEntity.setActive(true);
        savedEntity.setPassword(passwordEncoder.encode(savedEntity.getPassword()));
        savedEntity.setRestaurants(null);
        savedEntity.setUserType(null); // TODO para criar um enum e relacionar? A tbl userType só tera 3 linha certo?

        userSpringDataRepository.save(savedEntity);

        return USER_PERSISTENCE_MAPPER.mapToUser(savedEntity);
    }
}

