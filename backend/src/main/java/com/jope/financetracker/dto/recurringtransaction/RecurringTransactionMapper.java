package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.dto.transaction.TransactionMapper;
import com.jope.financetracker.model.RecurringTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface RecurringTransactionMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "isSubscription", target = "isSubscription")
    RecurringTransactionResponseDTO recurringTransactionToRecurringTransactionResponseDTO(RecurringTransaction recurringTransaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(source = "categoryId", target = "category.id")
    RecurringTransaction recurringTransactionRequestDTOToRecurringTransaction(RecurringTransactionRequestDTO recurringTransactionRequestDTO);
}
