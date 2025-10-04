package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.AddressesRestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.exception.NotFoundException;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.persistence.enums.AddressesOwnerTypeEnum;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.AddressesRestaurantPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.AddressesRestaurantSpringDataRepository;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.RestaurantSpringDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AddressesRestaurantGatewayImpl implements AddressesRestaurantRepositoryGateway {
    private final AddressesRestaurantSpringDataRepository addressesRestaurantSpringDataRepository;
    private final RestaurantSpringDataRepository restaurantSpringDataRepository;

    private static final AddressesRestaurantPersistenceMapper ADDRESSES_RESTAURANT_PERSISTENCE_MAPPER = AddressesRestaurantPersistenceMapper.INSTANCE;

    public AddressesRestaurantGatewayImpl(AddressesRestaurantSpringDataRepository addressesRestaurantSpringDataRepository,
                                          RestaurantSpringDataRepository restaurantSpringDataRepository) {
        this.addressesRestaurantSpringDataRepository = addressesRestaurantSpringDataRepository;
        this.restaurantSpringDataRepository = restaurantSpringDataRepository;
    }

    @Override
    public CreatedAddressesRestaurant createAddressesRestaurant(CreateAddressesRestaurant createAddressesRestaurant) {
        RestaurantEntity restaurantEntity = restaurantSpringDataRepository.findByIdAndActiveTrue(createAddressesRestaurant.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        AddressesEntity addressesEntity = ADDRESSES_RESTAURANT_PERSISTENCE_MAPPER.mapToAddressesEntity(createAddressesRestaurant);
        addressesEntity.setOwnerType(AddressesOwnerTypeEnum.RESTAURANT.getDescription());
        addressesEntity.setOwnerId(restaurantEntity.getId());
        addressesRestaurantSpringDataRepository.save(addressesEntity);

        restaurantEntity.setAddress(addressesEntity);
        restaurantSpringDataRepository.save(restaurantEntity);

        return ADDRESSES_RESTAURANT_PERSISTENCE_MAPPER.mapToCreatedAddressesRestaurant(addressesEntity);
    }

    @Transactional
    @Override
    public void deleteAddressesRestaurant(UUID restaurantId) {
        RestaurantEntity restaurantEntity = restaurantSpringDataRepository.findByIdAndActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        AddressesEntity addressesEntity = addressesRestaurantSpringDataRepository
                .findByOwnerTypeAndOwnerId(AddressesOwnerTypeEnum.RESTAURANT.getDescription(), restaurantId)
                .orElseThrow(() -> new NotFoundException("Address not found for restaurant"));

        restaurantEntity.setAddress(null);
        restaurantSpringDataRepository.save(restaurantEntity);

        addressesRestaurantSpringDataRepository.delete(addressesEntity);
    }
}
