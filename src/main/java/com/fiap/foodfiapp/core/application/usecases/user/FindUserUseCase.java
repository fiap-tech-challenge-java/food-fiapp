package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.entities.User;
import java.util.List;

public interface FindUserUseCase {
    List<User> findAll();
    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
