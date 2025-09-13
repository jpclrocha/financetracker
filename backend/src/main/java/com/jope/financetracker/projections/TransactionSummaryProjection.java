package com.jope.financetracker.projections;

import java.math.BigDecimal;

public interface TransactionSummaryProjection {
    BigDecimal getTotalIncome();
    BigDecimal getTotalExpenses();
    BigDecimal getNetBalance();
    Long getTransactionCount();
    Long getIncomeCount();
    Long getExpenseCount();
}
