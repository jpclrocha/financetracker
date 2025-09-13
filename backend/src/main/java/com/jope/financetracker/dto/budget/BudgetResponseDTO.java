package com.jope.financetracker.dto.budget;

import com.jope.financetracker.dto.category.CategoryResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record BudgetResponseDTO(
    Long id,
    String name,
    BigDecimal amount,
    LocalDate startPeriod,
    LocalDate endPeriod,
    Set<CategoryResponseDTO> categories
) {}
