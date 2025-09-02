package com.jope.financetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
}
