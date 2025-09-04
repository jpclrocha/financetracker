package com.jope.financetracker.dto.budget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BudgetRequestDTO(
    UUID costumerId,
    String name,
    BigDecimal amount,
    LocalDate startPeriod,
    LocalDate endPeriod
) {}
