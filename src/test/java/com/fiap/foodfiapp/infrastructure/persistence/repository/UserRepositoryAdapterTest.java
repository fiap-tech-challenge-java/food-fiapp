package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserSpringDataRepository userSpringDataRepository;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    private User user;
    private UserEntity userEntity;
    private UUID userId;
    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userTypeUuid = UUID.randomUUID();

        user = new User(
                userId,
                "Test User",
                "test@email.com",
                "testlogin",
                "12345678901",
                Collections.emptyList(),
                null,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "password123"
        );

        userEntity = UserEntity.builder()
                .id(userId)
                .name("Test User")
                .email("test@email.com")
                .login("testlogin")
                .cpf("12345678901")
                .password("password123")
                .active(true)
                .build();
    }

    @Test
    void shouldSaveUserSuccessfully() {
        when(userSpringDataRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User result = userRepositoryAdapter.save(user);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@email.com");
        assertThat(result.getLogin()).isEqualTo("testlogin");
        assertThat(result.getCpf()).isEqualTo("12345678901");
        assertThat(result.isActive()).isTrue();
        verify(userSpringDataRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        when(userSpringDataRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Optional<User> result = userRepositoryAdapter.findById(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(userId);
        assertThat(result.get().getName()).isEqualTo("Test User");
        verify(userSpringDataRepository).findById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        when(userSpringDataRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryAdapter.findById(userId);

        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findById(userId);
    }

    @Test
    void shouldFindUserByEmailSuccessfully() {
        String email = "test@email.com";
        when(userSpringDataRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        Optional<User> result = userRepositoryAdapter.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        verify(userSpringDataRepository).findByEmail(email);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        String email = "nonexistent@email.com";
        when(userSpringDataRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryAdapter.findByEmail(email);

        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findByEmail(email);
    }

    @Test
    void shouldFindAllUsersSuccessfully() {
        List<UserEntity> userEntities = Arrays.asList(userEntity);
        when(userSpringDataRepository.findAll()).thenReturn(userEntities);

        List<User> result = userRepositoryAdapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(userId);
        verify(userSpringDataRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        when(userSpringDataRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userRepositoryAdapter.findAll();

        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findAll();
    }

    @Test
    void shouldDeleteUserByIdSuccessfully() {
        doNothing().when(userSpringDataRepository).deleteById(userId);

        userRepositoryAdapter.deleteById(userId);

        verify(userSpringDataRepository).deleteById(userId);
    }

    @Test
    void shouldCheckIfUserExistsByUserTypeUuid() {
        when(userSpringDataRepository.existsByUserType_Uuid(userTypeUuid)).thenReturn(true);

        boolean result = userRepositoryAdapter.existsByUserTypeUuid(userTypeUuid);

        assertThat(result).isTrue();
        verify(userSpringDataRepository).existsByUserType_Uuid(userTypeUuid);
    }

    @Test
    void shouldReturnFalseWhenNoUserExistsWithUserTypeUuid() {
        when(userSpringDataRepository.existsByUserType_Uuid(userTypeUuid)).thenReturn(false);

        boolean result = userRepositoryAdapter.existsByUserTypeUuid(userTypeUuid);

        assertThat(result).isFalse();
        verify(userSpringDataRepository).existsByUserType_Uuid(userTypeUuid);
    }

    @Test
    void shouldHandleNullUserTypeInToEntity() {
        User userWithNullType = new User(
                userId, "Test User", "test@email.com", "testlogin", "12345678901",
                Collections.emptyList(), null, true, null, null, "password123"
        );

        when(userSpringDataRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User result = userRepositoryAdapter.save(userWithNullType);

        assertThat(result.getId()).isEqualTo(userId);
        verify(userSpringDataRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldHandleMultipleUsersInFindAll() {
        UserEntity secondUserEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("Second User")
                .email("second@email.com")
                .login("secondlogin")
                .cpf("98765432100")
                .password("password456")
                .active(false)
                .build();

        List<UserEntity> userEntities = Arrays.asList(userEntity, secondUserEntity);
        when(userSpringDataRepository.findAll()).thenReturn(userEntities);

        List<User> result = userRepositoryAdapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Test User");
        assertThat(result.get(1).getName()).isEqualTo("Second User");
        verify(userSpringDataRepository).findAll();
    }
}
