package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import java.util.Optional;

// Não implementa UserRepository do domínio diretamente para evitar conflitos de tipos.
// O adapter UserRepositoryAdapter faz a ponte entre UserSpringDataRepository e UserRepository.
public interface UserSpringDataRepository extends JpaRepository<UserEntity, Long>, Repository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
