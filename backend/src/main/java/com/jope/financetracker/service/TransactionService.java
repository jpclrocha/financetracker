package com.jope.financetracker.service;

import com.fasterxml.uuid.Generators;
import com.jope.financetracker.dto.transaction.InstallmentsTransactionRequestDTO;
import com.jope.financetracker.dto.transaction.TransactionRequestDTO;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.model.Transaction;
import com.jope.financetracker.repository.TransactionRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CostumerService costumerService;
    private final CategoryService categoryService;

    public TransactionService(TransactionRepository transactionRepository, CostumerService costumerService,
            CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.costumerService = costumerService;
        this.categoryService = categoryService;
    }

    public Transaction createTransaction(TransactionRequestDTO obj) {
        Costumer cos = costumerService.findById(obj.costumerId());
        Category cat = categoryService.findCategoryById(obj.categoryId(), obj.costumerId());
        Transaction t = new Transaction();
        t.setCostumer(cos);
        t.setCategory(cat);
        t.setAmount(obj.amount());
        t.setDate(obj.date());
        t.setDescription(obj.description());
        return transactionRepository.save(t);
    }

    public List<Transaction> createInstallmentsTransaction(InstallmentsTransactionRequestDTO obj) {
        UUID groupId = Generators.timeBasedEpochGenerator().generate();
        Costumer cos = costumerService.findById(obj.costumerId());
        Category cat = categoryService.findCategoryById(obj.categoryId(), obj.costumerId());
        BigDecimal installmentAmount = obj.amount()
                .divide(BigDecimal.valueOf(obj.installmentTotal()));

        List<Transaction> transactionsToSave = new ArrayList<>();

        for (int i = 1; i <= obj.installmentTotal(); i++) {
            Transaction transaction = new Transaction();
            transaction.setInstallmentGroupId(groupId);
            transaction.setInstallmentNumber(i);
            transaction.setInstallmentTotal(obj.installmentTotal());
            transaction.setCostumer(cos);
            transaction.setCategory(cat);
            transaction.setAmount(installmentAmount);
            transaction.setDescription(obj.description() + " (Parcela " + i + " de " + obj.installmentTotal() + ")");
            transaction.setDate(obj.date().plusMonths(i - Long.valueOf(1)));
            transactionsToSave.add(transaction);
        }

        return transactionRepository.saveAll(transactionsToSave);
    }

    public void deleteInstallmentsTransaction(UUID groupUUID) {
        if (groupUUID == null) {
            throw new IllegalArgumentException("Installments transaction group id must not be null!");
        }

        try {
            transactionRepository.deleteByInstallmentGroupId(groupUUID);       
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete transactions for groupUUID: " + groupUUID);
        }
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = getTransactionById(id);

        transaction.setCostumer(transactionDetails.getCostumer());
        transaction.setCategory(transactionDetails.getCategory());
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
