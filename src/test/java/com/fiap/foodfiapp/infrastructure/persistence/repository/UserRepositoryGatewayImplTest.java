package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypeEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryGatewayImplTest {

    @Mock
    private UserSpringDataRepository userSpringDataRepository;

    @InjectMocks
    private UserRepositoryGatewayImpl userRepositoryGateway;

    private User user;
    private UserEntity userEntity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");

        UserTypeEntity userTypeEntity = UserTypeEntity.builder()
                .uuid(userType.getUuid())
                .name(userType.getName())
                .build();

        user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "johndoe",
                "12345678901",
                null,
                userType,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        userEntity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .cpf("12345678901")
                .password("password123")
                .active(true)
                .userType(userTypeEntity)
                .build();
    }

    @Test
    void shouldSaveUser() {
        when(userSpringDataRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User savedUser = userRepositoryGateway.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(userId);
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        verify(userSpringDataRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserById() {
        when(userSpringDataRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Optional<User> foundUser = userRepositoryGateway.findById(userId);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(userId);
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
        verify(userSpringDataRepository).findById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        when(userSpringDataRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepositoryGateway.findById(userId);

        assertThat(foundUser).isEmpty();
        verify(userSpringDataRepository).findById(userId);
    }

    @Test
    void shouldFindUserByEmail() {
        String email = "john@example.com";
        when(userSpringDataRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        Optional<User> foundUser = userRepositoryGateway.findByEmail(email);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(email);
        verify(userSpringDataRepository).findByEmail(email);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        String email = "nonexistent@example.com";
        when(userSpringDataRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepositoryGateway.findByEmail(email);

        assertThat(foundUser).isEmpty();
        verify(userSpringDataRepository).findByEmail(email);
    }

    @Test
    void shouldFindAllUsers() {
        UserTypeEntity anotherUserTypeEntity = UserTypeEntity.builder()
                .uuid(UUID.randomUUID())
                .name("ADMIN")
                .build();

        UserEntity anotherEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .email("jane@example.com")
                .login("janedoe")
                .userType(anotherUserTypeEntity)
                .build();

        when(userSpringDataRepository.findAll()).thenReturn(Arrays.asList(userEntity, anotherEntity));

        List<User> users = userRepositoryGateway.findAll();

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo("John Doe");
        assertThat(users.get(1).getName()).isEqualTo("Jane Doe");
        verify(userSpringDataRepository).findAll();
    }

    @Test
    void shouldDeleteUserById() {
        doNothing().when(userSpringDataRepository).deleteById(userId);

        userRepositoryGateway.deleteById(userId);

        verify(userSpringDataRepository).deleteById(userId);
    }

    @Test
    void shouldCheckIfExistsByUserTypeUuid() {
        UUID userTypeUuid = UUID.randomUUID();
        when(userSpringDataRepository.existsByUserType_Uuid(userTypeUuid)).thenReturn(true);

        boolean exists = userRepositoryGateway.existsByUserTypeUuid(userTypeUuid);

        assertThat(exists).isTrue();
        verify(userSpringDataRepository).existsByUserType_Uuid(userTypeUuid);
    }

    @Test
    void shouldReturnFalseWhenUserTypeDoesNotExist() {
        UUID userTypeUuid = UUID.randomUUID();
        when(userSpringDataRepository.existsByUserType_Uuid(userTypeUuid)).thenReturn(false);

        boolean exists = userRepositoryGateway.existsByUserTypeUuid(userTypeUuid);

        assertThat(exists).isFalse();
        verify(userSpringDataRepository).existsByUserType_Uuid(userTypeUuid);
    }
}
