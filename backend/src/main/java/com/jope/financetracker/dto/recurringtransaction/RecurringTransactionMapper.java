package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.model.RecurringTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecurringTransactionMapper {

    @Mapping(source = "category.name", target = "categoryName")
    RecurringTransactionResponseDTO recurringTransactionToRecurringTransactionResponseDTO(RecurringTransaction recurringTransaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costumer", ignore = true)
    @Mapping(source = "categoryId", target = "category.id")
    RecurringTransaction recurringTransactionRequestDTOToRecurringTransaction(RecurringTransactionRequestDTO recurringTransactionRequestDTO);
}
