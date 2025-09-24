package com.jope.financetracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jope.financetracker.projections.TransactionSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    void deleteByInstallmentGroupId(UUID groupId);
    List<Transaction> findAllByCustomerId(UUID id);
    Page<Transaction> findAllByCustomerId(UUID id, Pageable pageable);
    List<Transaction> findAllByInstallmentGroupId(UUID uuid);
    List<Transaction> findAllByCustomerIdAndInstallmentGroupIdNotNull(UUID customerId);

    @Query("SELECT t FROM Transaction t WHERE t.customer.id = :customerId AND t.date BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsForCustomerBetweenDates(
            @Param("customerId") UUID customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query(value = """
        SELECT\s
            COALESCE(SUM(CASE WHEN c.type = 'INCOME' THEN t.amount ELSE 0 END),0) AS totalIncome,
            COALESCE(SUM(CASE WHEN c.type = 'EXPENSE' THEN t.amount ELSE 0 END),0) AS totalExpenses,
            COALESCE(SUM(CASE WHEN c.type = 'INCOME' THEN t.amount ELSE -t.amount END),0) AS netBalance,
            COUNT(*) AS transactionCount,
            SUM(CASE WHEN c.type = 'INCOME' THEN 1 ELSE 0 END) AS incomeCount,
            SUM(CASE WHEN c.type = 'EXPENSE' THEN 1 ELSE 0 END) AS expenseCount
        FROM transaction t
        JOIN category c ON t.category_id = c.id
        WHERE t.customer_id = :customerId
          AND t.date BETWEEN :startDate AND :endDate
       \s""", nativeQuery = true)
    TransactionSummaryProjection getSummary(
            @Param("customerId") UUID customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT MAX(t.date) FROM Transaction t WHERE t.recurringTransaction.id = :recurringId")
    Optional<LocalDate> findMostRecentTransactionDateByRecurringId(@Param("recurringId") Long recurringId);
}
