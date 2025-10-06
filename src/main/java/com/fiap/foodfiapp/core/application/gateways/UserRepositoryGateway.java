package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.port.UserRepository;

/**
 * This is a gateway interface for UserRepository, to be implemented by infrastructure layer (e.g., database, external services).
 * It extends the domain port to allow for separation of concerns and easier testing/mocking.
 */
public interface UserRepositoryGateway extends UserRepository {
    // You can add gateway-specific methods here if needed in the future
}

