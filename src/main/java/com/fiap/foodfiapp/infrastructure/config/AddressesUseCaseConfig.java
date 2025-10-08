package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.addresses.*;
import com.fiap.foodfiapp.core.application.usecases.addresses.impl.*;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para expor os casos de uso (Use Cases) de Endereço
 * como Beans gerenciáveis pelo Spring.
 * Isso permite que o Spring injete essas dependências onde forem necessárias,
 * como nos Controllers.
 */
@Configuration
public class AddressesUseCaseConfig {

    /**
     * Define o bean para o caso de uso de criação de endereço.
     * O Spring injetará automaticamente a dependência 'AddressRepository'
     * porque a implementação 'AddressRepositoryImpl' está anotada com @Component.
     */
    @Bean
    public CreateAddressesUseCase createAddressUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository,
            ValidateOwnerUseCase validateOwnerUseCase) {
        return new CreateAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);
    }

    /**
     * Define o bean para o caso de uso de atualização de endereço.
     */
    @Bean
    public UpdateAddressesUseCase updateAddressUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository,
            ValidateOwnerUseCase validateOwnerUseCase) {
        return new UpdateAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);
    }

    /**
     * Define o bean para o caso de uso de exclusão de endereço.
     */
    @Bean
    public DeleteAddressesUseCase deleteAddressUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository,
            ValidateOwnerUseCase validateOwnerUseCase) {
        return new DeleteAddressesUseCaseImpl(addressRepository, validateOwnerUseCase);
    }

    /**
     * Define o bean para o caso de uso de busca de endereços por proprietário.
     */
    @Bean
    public FindAddressesByOwnerUseCase findAddressByOwnerUseCase(AddressRepository addressRepository,
            UserRepository userRepository, RestaurantRepository restaurantRepository) {
        return new FindAddressesByOwnerUseCaseImpl(addressRepository);
    }

    /**
     * Define o bean para o caso de uso de validação de proprietário de endereço.
     */
    @Bean
    public ValidateOwnerUseCase validateOwnerUseCase(UserRepository userRepository,
            RestaurantRepository restaurantRepository) {
        return new ValidateOwnerUseCaseImpl(userRepository, restaurantRepository);
    }
}