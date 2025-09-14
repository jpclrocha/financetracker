package com.jope.financetracker.dto.category;

import com.jope.financetracker.enums.ExpenseType;

public record CategoryResponseDTO(
    Long id,
    String name,
    ExpenseType type,
    boolean isPublic
) {}
