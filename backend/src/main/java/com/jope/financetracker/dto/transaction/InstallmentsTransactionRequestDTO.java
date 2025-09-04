package com.jope.financetracker.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentsTransactionRequestDTO(
    UUID costumerId,
    Long categoryId,
    BigDecimal amount,
    LocalDate date,
    String description,
    Integer installmentTotal
) {}