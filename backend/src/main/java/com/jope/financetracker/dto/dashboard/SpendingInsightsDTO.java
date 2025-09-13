package com.jope.financetracker.dto.dashboard;

public record SpendingInsightsDTO(
        CategorySummaryDTO biggestExpenseCategory,
        CategorySummaryDTO leastExpenseCategory) {
}
