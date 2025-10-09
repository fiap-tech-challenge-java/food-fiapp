package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypeEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserTypeSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(UserTypeRepositoryImpl.class)
class UserTypeRepositoryImplIntegrationTest {

    @Autowired
    private UserTypeRepositoryImpl userTypeRepository;

    @Autowired
    private UserTypeSpringDataRepository springDataRepository;

    private UserType userType;

    @BeforeEach
    void setUp() {
        springDataRepository.deleteAll();
        
        userType = new UserType();
        userType.setUuid(UUID.randomUUID());
        userType.setName("CLIENT");
        userType.setIsActive(true);
        userType.setCreatedAt(OffsetDateTime.now());
        userType.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void shouldSaveUserType() {
        // When
        UserType saved = userTypeRepository.save(userType);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getUuid()).isNotNull();
        assertThat(saved.getName()).isEqualTo("CLIENT");
        assertThat(saved.getIsActive()).isTrue();
    }

    @Test
    void shouldFindUserTypeById() {
        // Given
        UserType saved = userTypeRepository.save(userType);

        // When
        Optional<UserType> found = userTypeRepository.findById(saved.getUuid());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldReturnEmptyWhenUserTypeNotFoundById() {
        // When
        Optional<UserType> found = userTypeRepository.findById(UUID.randomUUID());

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindUserTypeByName() {
        // Given
        userTypeRepository.save(userType);

        // When
        Optional<UserType> found = userTypeRepository.findByName("CLIENT");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("CLIENT");
    }

    @Test
    void shouldReturnEmptyWhenUserTypeNotFoundByName() {
        // When
        Optional<UserType> found = userTypeRepository.findByName("NONEXISTENT");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllUserTypes() {
        // Given
        userTypeRepository.save(userType);
        
        UserType anotherUserType = new UserType();
        anotherUserType.setUuid(UUID.randomUUID());
        anotherUserType.setName("ADMIN");
        anotherUserType.setIsActive(true);
        anotherUserType.setCreatedAt(OffsetDateTime.now());
        anotherUserType.setUpdatedAt(OffsetDateTime.now());
        userTypeRepository.save(anotherUserType);

        // When
        List<UserType> all = userTypeRepository.findAll();

        // Then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(UserType::getName)
                .containsExactlyInAnyOrder("CLIENT", "ADMIN");
    }

    @Test
    void shouldDeleteUserTypeById() {
        // Given
        UserType saved = userTypeRepository.save(userType);
        UUID id = saved.getUuid();

        // When
        userTypeRepository.deleteById(id);

        // Then
        Optional<UserType> found = userTypeRepository.findById(id);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoUserTypes() {
        // When
        List<UserType> all = userTypeRepository.findAll();

        // Then
        assertThat(all).isEmpty();
    }
}
