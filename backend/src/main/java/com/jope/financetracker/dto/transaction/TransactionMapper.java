package com.jope.financetracker.dto.transaction;

import com.jope.financetracker.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "isInstallment", expression = "java(transaction.getInstallmentGroupId() != null)")
    TransactionResponseDTO transactionToTransactionResponseDTO(Transaction transaction);

    @Mapping(source = "costumerId", target = "costumer.id")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "installmentGroupId", ignore = true)
    @Mapping(target = "installmentNumber", ignore = true)
    @Mapping(target = "installmentTotal", ignore = true)
    Transaction transactionRequestDTOToTransaction(TransactionRequestDTO transactionRequestDTO);

    @Mapping(source = "category.name", target = "categoryName")
    InstallmentsTransactionResponseDTO installmentTransactionToTransactionResponseDTO(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "installmentGroupId", ignore = true)
    @Mapping(target = "installmentNumber", ignore = true)
    @Mapping(source = "costumerId", target = "costumer.id")
    @Mapping(source = "categoryId", target = "category.id")
    Transaction installmentTransactionRequestDTOToTransaction(InstallmentsTransactionRequestDTO transactionRequestDTO);
}
