package com.jope.financetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.RecurringTransaction;

public interface RecurringTransactionRepository  extends JpaRepository<RecurringTransaction, Long> {
    
}
