package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.AddressesRestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.exception.NotFoundException;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.AddressesRestaurantPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.AddressesRestaurantSpringDataRepository;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.RestaurantSpringDataRepository;
import org.springframework.stereotype.Service;

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
        RestaurantEntity restaurantEntity = restaurantSpringDataRepository.findById(createAddressesRestaurant.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        AddressesEntity addressesEntity = ADDRESSES_RESTAURANT_PERSISTENCE_MAPPER.mapToAddressesEntity(createAddressesRestaurant);

//        AddressesEntity addressesEntity = new AddressesEntity();
//        addressesEntity.setPublicPlace(createdAddressesRestaurant.publicPlace());
//        addressesEntity.setNumber(createdAddressesRestaurant.number());
//        addressesEntity.setComplement(createdAddressesRestaurant.complement());
//        addressesEntity.setNeighborhood(createdAddressesRestaurant.neighborhood());
//        addressesEntity.setCity(createdAddressesRestaurant.city());
//        addressesEntity.setState(createdAddressesRestaurant.state());
//        addressesEntity.setPostalCode(createdAddressesRestaurant.postalCode());
//        addressesEntity.setRestaurant(restaurantEntity);
        addressesRestaurantSpringDataRepository.save(addressesEntity);

        restaurantEntity.setAddress(addressesEntity);
        restaurantSpringDataRepository.save(restaurantEntity);
        return null;
    }
}
