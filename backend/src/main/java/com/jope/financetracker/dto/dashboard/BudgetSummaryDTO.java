package com.jope.financetracker.dto.dashboard;

import java.math.BigDecimal;

public record BudgetSummaryDTO(
        String name,
        BigDecimal amount,
        BigDecimal amountSpent,
        BigDecimal amountRemaining,
        Double usagePercent) {
}
