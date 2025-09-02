package com.jope.financetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
