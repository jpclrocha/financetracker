package com.jope.financetracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    void deleteByInstallmentGroupId(UUID groupId);
    List<Transaction> findAllByCostumerId(UUID id);
    List<Transaction> findAllByInstallmentGroupId(UUID uuid);
}
