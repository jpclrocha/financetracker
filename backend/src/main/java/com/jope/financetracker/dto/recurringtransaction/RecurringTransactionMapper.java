package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.model.RecurringTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecurringTransactionMapper {

    RecurringTransactionMapper INSTANCE = Mappers.getMapper(RecurringTransactionMapper.class);

    @Mapping(source = "category.name", target = "categoryName")
    RecurringTransactionResponseDTO recurringTransactionToRecurringTransactionResponseDTO(RecurringTransaction recurringTransaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "costumerId", target = "costumer.id")
    @Mapping(source = "categoryId", target = "category.id")
    RecurringTransaction recurringTransactionRequestDTOToRecurringTransaction(RecurringTransactionRequestDTO recurringTransactionRequestDTO);
}
