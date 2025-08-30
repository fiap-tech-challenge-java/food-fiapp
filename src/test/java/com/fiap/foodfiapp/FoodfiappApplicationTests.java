package com.fiap.foodfiapp;

import com.fiap.foodfiapp.core.application.usecases.user.impl.CreateUserUseCaseImpl;
import com.fiap.foodfiapp.infrastructure.persistence.repository.UserRepositoryGatewayImpl;
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
	private CreateUserUseCaseImpl createUserUseCaseImpl;
	@Autowired
	private UserRepositoryGatewayImpl userRepositoryGatewayImpl;

//	@Test
//	void contextLoads() {
//		assertThat(userController).isNotNull();
//		assertThat(createUserUseCaseImpl).isNotNull();
//		assertThat(userRepositoryGatewayImpl).isNotNull();
//	}
}
