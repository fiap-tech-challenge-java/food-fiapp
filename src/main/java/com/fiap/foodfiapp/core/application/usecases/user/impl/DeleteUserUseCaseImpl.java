// src/main/java/com/fiap/foodfiapp/core/application/usecases/user/impl/DeleteUserUseCaseImpl.java
package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.DeleteUserUseCase;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(UUID id) {
        // RN: Adicionar verificação se o usuário existe antes de deletar
        if (!userRepository.findById(id).isPresent()) {
            // Opcional: Lançar exceção se o usuário não for encontrado
            // throw new UserNotFoundException("User with id " + id + " not found.");
            return; // Ou simplesmente retornar se a operação for idempotente
        }
        userRepository.deleteById(id);
    }
}