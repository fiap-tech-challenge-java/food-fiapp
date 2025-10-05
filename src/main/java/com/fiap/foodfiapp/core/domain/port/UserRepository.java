package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.User;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);
    Optional<User> findByLogin(String login);
    List<User> findAll();
    void deleteById(UUID id);
    boolean existsByUserTypeUuid(UUID userTypeUuid);
}
