package com.jope.financetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.RecurringTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RecurringTransactionRepository  extends JpaRepository<RecurringTransaction, Long> {
    List<RecurringTransaction> findAllByCustomerId(UUID id);
    List<RecurringTransaction> findByNextDueDateAndIsActiveTrue(LocalDate date);
}
