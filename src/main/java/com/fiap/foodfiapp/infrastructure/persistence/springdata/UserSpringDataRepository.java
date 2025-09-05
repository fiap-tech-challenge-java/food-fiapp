package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// Não implementa UserRepository do domínio diretamente para evitar conflitos de tipos.
// O adapter UserRepositoryAdapter faz a ponte entre UserSpringDataRepository e UserRepository.

@Repository
public interface UserSpringDataRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}
