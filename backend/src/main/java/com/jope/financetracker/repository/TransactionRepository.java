package com.jope.financetracker.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    void deleteByInstallmentGroupId(UUID groupId);

}
