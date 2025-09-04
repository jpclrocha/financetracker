package com.jope.financetracker.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
    Long id,
    String categoryName,
    BigDecimal amount,
    LocalDate date,
    String description
) {}