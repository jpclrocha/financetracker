package com.jope.financetracker.dto.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRequestDTO(
    String name,
    BigDecimal amount,
    LocalDate startPeriod,
    LocalDate endPeriod
) {}
