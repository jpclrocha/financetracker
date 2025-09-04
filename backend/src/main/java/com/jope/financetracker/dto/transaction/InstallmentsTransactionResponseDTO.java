package com.jope.financetracker.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentsTransactionResponseDTO(
    Long id,
    String categoryName,
    BigDecimal amount,
    LocalDate date,
    String description,
    Integer installmentNumber,
    Integer installmentTotal
) {}