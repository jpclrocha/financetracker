package com.jope.financetracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByCustomerId(UUID costumerId);
}
