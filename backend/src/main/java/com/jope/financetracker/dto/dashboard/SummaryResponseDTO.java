package com.jope.financetracker.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record SummaryResponseDTO(
        String summaryPeriod,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netBalance,
        TransactionTypeSummaryDTO transactions,
        List<BudgetSummaryDTO> budgets,
        SpendingInsightsDTO spendingInsights
) {
}
