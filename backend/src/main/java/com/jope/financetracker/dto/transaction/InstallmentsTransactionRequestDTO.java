package com.jope.financetracker.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentsTransactionRequestDTO(
        Long categoryId,
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Date must not be null")
        LocalDate date,
        String description,
        @Min(value = 2, message = "Minimal installments is 2")
        Integer installmentTotal
) {}