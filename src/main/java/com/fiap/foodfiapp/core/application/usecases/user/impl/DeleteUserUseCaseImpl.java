package com.fiap.foodfiapp.core.application.usecases.user.impl;

import com.fiap.foodfiapp.core.application.usecases.user.DeleteUserUseCase;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public DeleteUserUseCaseImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void execute(UUID id) {
        if (id == null) {
            return;
        }
        
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return;
        }

        var user = optionalUser.get();
        if (Boolean.FALSE.equals(user.getIsActive())) {
            return;
        }

        user.setIsActive(false);
        userRepository.save(user);

        var ownerType = AddressOwnerTypeEnum.USER.getDescription();
        var addresses = addressRepository.findByOwner(id, ownerType);
        for (var address : addresses) {
            if (Boolean.TRUE.equals(address.getIsActive())) {
                address.setIsActive(false);
                addressRepository.save(address, id, ownerType);
            }
        }
    }
}