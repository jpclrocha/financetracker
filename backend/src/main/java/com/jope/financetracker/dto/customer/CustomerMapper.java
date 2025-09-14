package com.jope.financetracker.dto.customer;

import com.jope.financetracker.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDTO costumerToCostumerResponseDTO(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "recurringTransactions", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "currency", expression = "java(com.jope.financetracker.enums.Currency.valueOf(customerRequestDTO.currency()))")
    Customer costumerRequestDTOToCostumer(CustomerRequestDTO customerRequestDTO);
}
