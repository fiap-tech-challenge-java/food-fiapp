package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.model.AddressesResponse;
import com.fiap.foodfiapp.model.CreateAddressesRequest;
import com.fiap.foodfiapp.model.UpdateAddressesRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressesMapper {
    AddressesMapper INSTANCE = Mappers.getMapper(AddressesMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Addresses toAddress(CreateAddressesRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Addresses toAddress(UpdateAddressesRequest dto);

    AddressesResponse toAddressesResponse(Addresses addresses);

    List<AddressesResponse> toAddressesResponseList(List<Addresses> addresses);
}