package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.user.*;
import com.fiap.foodfiapp.core.application.usecases.user.impl.*;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, UserTypeRepository userTypeRepository,
            AddressRepository addressRepository) {
        return new CreateUserUseCaseImpl(userRepository, userTypeRepository, addressRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository, UserTypeRepository userTypeRepository) {
        return new UpdateUserUseCaseImpl(userRepository, userTypeRepository);
    }

    @Bean
    public FindUserUseCase findUserUseCase(UserRepository userRepository, AddressRepository addressRepository) {
        return new FindUserUseCaseImpl(userRepository, addressRepository);
    }

    @Bean
    public FindUserByEmailUseCase findUserByEmailUseCase(UserRepository userRepository,
            AddressRepository addressRepository) {
        return new FindByEmailUseCaseImpl(userRepository, addressRepository);
    }

    @Bean
    public FindUserByUsernameUseCase findUserByUsernameUseCase(UserRepository userRepository,
            AddressRepository addressRepository) {
        return new FindUserByUsernameUseCaseImpl(userRepository, addressRepository);
    }

    @Bean
    public FindAllUserUseCase findAllUserUseCase(UserRepository userRepository, AddressRepository addressRepository) {
        return new FindAllUserUseCaseImpl(userRepository, addressRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository, AddressRepository addressRepository) {
        return new DeleteUserUseCaseImpl(userRepository, addressRepository);
    }

    @Bean
    public IsAdminUseCase isAdminUseCase(UserRepository userRepository) {
        return new IsAdminUseCaseImpl(userRepository);
    }

    @Bean
    public ValidateAdminOrOwnerUseCase validateAdminOrOwnerUseCase(UserRepository userRepository) {
        return new ValidateAdminOrOwnerUseCaseImpl(userRepository);
    }
}