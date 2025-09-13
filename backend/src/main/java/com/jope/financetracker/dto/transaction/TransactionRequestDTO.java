package com.jope.financetracker.dto.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
    Long categoryId,
    @Positive(message = "Amount must be greater than zero")
    BigDecimal amount,
    @NotNull(message = "Date must not be null")
    LocalDate date,
    String description
) {}