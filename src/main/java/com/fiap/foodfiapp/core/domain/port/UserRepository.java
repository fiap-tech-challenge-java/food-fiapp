package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.User;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);
}

