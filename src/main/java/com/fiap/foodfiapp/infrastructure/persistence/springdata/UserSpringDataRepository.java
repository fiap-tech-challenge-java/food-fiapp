package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserSpringDataRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}

