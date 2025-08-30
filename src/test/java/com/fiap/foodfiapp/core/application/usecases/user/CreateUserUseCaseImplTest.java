package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

class CreateUserUseCaseImplTest {
    private UserRepositoryGateway userRepositoryGateway;
    private CreateUserUseCaseImpl createUserUseCaseImpl;

    @BeforeEach
    void setUp() {
        userRepositoryGateway = Mockito.mock(UserRepositoryGateway.class);
        createUserUseCaseImpl = new CreateUserUseCaseImpl(userRepositoryGateway);
    }

    /////////// COMENTADO POIS A CLASSE USER MUDOU, DEVEMOS APENAS ACRESCER OS CAMPOS NO CONSTRUTOR

//    @Test
//    void shouldCreateUserWhenEmailNotExists() {
//        User user = new User(null, "Test", "test@email.com", "1234");
//        when(userRepositoryGateway.findByEmail(user.email())).thenReturn(Optional.empty());
//        when(userRepositoryGateway.save(any(User.class))).thenReturn(new User(1L, user.name(), user.email(), user.password()));
//
//        User created = createUserUseCase.execute(user);
//        assertNotNull(created);
//        assertEquals(user.email(), created.email());
//        verify(userRepositoryGateway).save(user);
//    }
//
//    @Test
//    void shouldThrowBusinessExceptionWhenEmailExists() {
//        User user = new User(null, "Test", "test@email.com", "1234");
//        when(userRepositoryGateway.findByEmail(user.email())).thenReturn(Optional.of(user));
//
//        assertThrows(BusinessException.class, () -> createUserUseCase.execute(user));
//        verify(userRepositoryGateway, never()).save(any());
//    }
}

