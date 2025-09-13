package com.jope.financetracker.dto.budget;

import com.jope.financetracker.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    BudgetResponseDTO budgetToBudgetResponseDTO(Budget budget);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Budget budgetRequestDTOToBudget(BudgetRequestDTO budgetRequestDTO);
}
