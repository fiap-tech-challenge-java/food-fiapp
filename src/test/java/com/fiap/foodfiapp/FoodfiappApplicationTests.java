package com.fiap.foodfiapp;

import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.infrastructure.rest.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class FoodfiappApplicationTests {

	@Autowired
	private UserController userController;
	@Autowired
	private CreateUserUseCase createUserUseCase;
	@Autowired
	private UserRepositoryGatewayImpl userRepositoryGatewayImpl;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
		assertThat(createUserUseCase).isNotNull();
		assertThat(userRepositoryGatewayImpl).isNotNull();
	}
}
