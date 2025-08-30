package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.entities.CreateUser;
import com.fiap.foodfiapp.core.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is a gateway interface for UserRepository, to be implemented by infrastructure layer (e.g., database, external services).
 * It extends the domain port to allow for separation of concerns and easier testing/mocking.
 */
public interface UserRepositoryGateway {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(UUID id);
    User createUser(CreateUser createUser);
}

