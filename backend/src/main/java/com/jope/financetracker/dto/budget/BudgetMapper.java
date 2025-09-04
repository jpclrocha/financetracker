package com.jope.financetracker.dto.budget;

import com.jope.financetracker.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BudgetMapper {

    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    BudgetResponseDTO budgetToBudgetResponseDTO(Budget budget);

    @Mapping(source = "costumerId", target = "costumer.id")
    @Mapping(target = "id", ignore = true)
    Budget budgetRequestDTOToBudget(BudgetRequestDTO budgetRequestDTO);
}
