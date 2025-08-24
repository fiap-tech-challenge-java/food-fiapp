package com.fiap.foodfiapp.application.gateways;

import com.fiap.foodfiapp.domain.entity.User;
import com.fiap.foodfiapp.domain.port.UserRepository;
import java.util.Optional;
import java.util.List;

/**
 * This is a gateway interface for UserRepository, to be implemented by infrastructure layer (e.g., database, external services).
 * It extends the domain port to allow for separation of concerns and easier testing/mocking.
 */
public interface UserRepositoryGateway extends UserRepository {
    // You can add gateway-specific methods here if needed in the future
}

