package com.jope.financetracker.service;

import com.jope.financetracker.dto.dashboard.*;
import com.jope.financetracker.enums.ExpenseType;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Transaction;
import com.jope.financetracker.projections.TransactionSummaryProjection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final CurrentUserService currentUserService;
    private final TransactionService transactionService;
    private final BudgetService budgetService;

    public DashboardService(CurrentUserService currentUserService, TransactionService transactionService, BudgetService budgetService) {
        this.currentUserService = currentUserService;
        this.transactionService = transactionService;
        this.budgetService = budgetService;
    }

    public SummaryResponseDTO getSummary(SummaryRequestDTO obj){
        LocalDate endDate;
        if(obj.endDate() == null){
            LocalDate initial = LocalDate.now();
            endDate = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()));
        } else {
            endDate = obj.endDate();
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        String spendingPeriod = dtf.format(obj.startDate()).concat(" - ").concat(dtf.format(endDate));

        List<Transaction> transactions = transactionService.getTransactionsForCustomerBetweenDates(
                        currentUserService.getCurrentUserId(),
                        obj.startDate(),
                        endDate);


        TransactionSummaryProjection summary = transactionService.getTransactionSummary(currentUserService.getCurrentUserId(), obj.startDate(), endDate);

        BigDecimal totalIncome = summary.getTotalIncome();
        BigDecimal totalExpenses = summary.getTotalExpenses();
        BigDecimal netBalance = summary.getNetBalance();
        Long transactionCount = summary.getTransactionCount();
        Long incomeTransactionCount = summary.getIncomeCount();
        Long expenseTransactionCount = summary.getExpenseCount();

        List<Budget> budgets = budgetService.getAllBudgets();

        Map<Category, BigDecimal> spendingPerCategory = transactions.stream()
                .filter(tx -> tx.getCategory().getType() == ExpenseType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        List<BudgetSummaryDTO> budgetSummaryDTOs = new ArrayList<>();
        for (Budget budget : budgets) {
            BigDecimal amount = budget.getAmount();

            // sum only categories belonging to this budget
            BigDecimal amountSpentInBudget = budget.getCategories().stream() // <-- use budget.categories
                    .map(cat -> spendingPerCategory.getOrDefault(cat, BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal amountRemaining = amount.subtract(amountSpentInBudget);
            Double usage = amountSpentInBudget
                    .divide(amount, 2, RoundingMode.HALF_UP)
                    .doubleValue();

            budgetSummaryDTOs.add(new BudgetSummaryDTO(
                    budget.getName(),
                    amount,
                    amountSpentInBudget,
                    amountRemaining,
                    usage
            ));
        }


        var biggestExpense = spendingPerCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        var leastExpense = spendingPerCategory.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);

        SpendingInsightsDTO insightsDTO = new SpendingInsightsDTO(
                biggestExpense != null ?
                        new CategorySummaryDTO(biggestExpense.getKey().getName(), biggestExpense.getValue())
                        : null,
                leastExpense != null ?
                        new CategorySummaryDTO(leastExpense.getKey().getName(), leastExpense.getValue())
                        : null
        );

        return new SummaryResponseDTO(
                spendingPeriod,
                totalIncome,
                totalExpenses,
                netBalance,
                new TransactionTypeSummaryDTO(
                        transactionCount,
                        incomeTransactionCount,
                        expenseTransactionCount
                ),
                budgetSummaryDTOs,
                insightsDTO
        );
    }
}
