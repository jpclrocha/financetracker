package com.jope.financetracker.dto.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetResponseDTO(
    Long id,
    String name,
    BigDecimal amount,
    LocalDate startPeriod,
    LocalDate endPeriod
) {}
