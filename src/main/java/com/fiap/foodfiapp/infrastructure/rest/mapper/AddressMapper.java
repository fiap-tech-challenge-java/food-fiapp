package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.infrastructure.rest.dto.address.AddressResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.address.CreateAddressRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.address.UpdateAddressRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toAddress(CreateAddressRequestDTO dto);
    Address toAddress(UpdateAddressRequestDTO dto);
    AddressResponseDTO toAddressResponseDTO(Address address);
    List<AddressResponseDTO> toAddressResponseDTOList(List<Address> addresses);
}