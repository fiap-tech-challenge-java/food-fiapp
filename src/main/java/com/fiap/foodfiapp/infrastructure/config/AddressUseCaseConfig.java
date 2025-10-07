package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.address.*;
import com.fiap.foodfiapp.core.application.usecases.address.impl.*;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para expor os casos de uso (Use Cases) de Endereço
 * como Beans gerenciáveis pelo Spring.
 * Isso permite que o Spring injete essas dependências onde forem necessárias,
 * como nos Controllers.
 */
@Configuration
public class AddressUseCaseConfig {

    /**
     * Define o bean para o caso de uso de criação de endereço.
     * O Spring injetará automaticamente a dependência 'AddressRepository'
     * porque a implementação 'AddressRepositoryImpl' está anotada com @Component.
     */
    @Bean
    public CreateAddressUseCase createAddressUseCase(AddressRepository addressRepository) {
        return new CreateAddressUseCaseImpl(addressRepository);
    }

    /**
     * Define o bean para o caso de uso de atualização de endereço.
     */
    @Bean
    public UpdateAddressUseCase updateAddressUseCase(AddressRepository addressRepository) {
        return new UpdateAddressUseCaseImpl(addressRepository);
    }

    /**
     * Define o bean para o caso de uso de exclusão de endereço.
     */
    @Bean
    public DeleteAddressUseCase deleteAddressUseCase(AddressRepository addressRepository) {
        return new DeleteAddressUseCaseImpl(addressRepository);
    }

    /**
     * Define o bean para o caso de uso de busca de endereços por proprietário.
     */
    @Bean
    public FindAddressByOwnerUseCase findAddressByOwnerUseCase(AddressRepository addressRepository) {
        return new FindAddressByOwnerUseCaseImpl(addressRepository);
    }
}