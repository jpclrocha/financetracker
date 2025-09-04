package com.jope.financetracker.dto.transaction;

import com.jope.financetracker.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "category.name", target = "categoryName")
    TransactionResponseDTO transactionToTransactionResponseDTO(Transaction transaction);

    @Mapping(source = "costumerId", target = "costumer.id")
    @Mapping(source = "categoryId", target = "category.id")
    Transaction transactionRequestDTOToTransaction(TransactionRequestDTO transactionRequestDTO);
}
