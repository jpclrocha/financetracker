package com.jope.financetracker.dto.costumer;

import com.jope.financetracker.model.Costumer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CostumerMapper {

    CostumerResponseDTO costumerToCostumerResponseDTO(Costumer costumer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "recurringTransactions", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "currency", expression = "java(com.jope.financetracker.enums.Currency.valueOf(costumerRequestDTO.currency()))")
    Costumer costumerRequestDTOToCostumer(CostumerRequestDTO costumerRequestDTO);
}
