package com.jope.financetracker.dto.costumer;

import com.jope.financetracker.model.Costumer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CostumerMapper {

    CostumerMapper INSTANCE = Mappers.getMapper(CostumerMapper.class);

    CostumerResponseDTO costumerToCostumerResponseDTO(Costumer costumer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "recurringTransactions", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    Costumer costumerRequestDTOToCostumer(CostumerRequestDTO costumerRequestDTO);
}
