package com.jope.financetracker.service;

import com.fasterxml.uuid.Generators;
import com.jope.financetracker.dto.transaction.InstallmentsTransactionRequestDTO;
import com.jope.financetracker.dto.transaction.TransactionRequestDTO;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.Transaction;
import com.jope.financetracker.projections.TransactionSummaryProjection;
import com.jope.financetracker.repository.TransactionRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final CategoryService categoryService;
    private final CurrentUserService currentUserService;

    public TransactionService(TransactionRepository transactionRepository, CustomerService customerService,
            CategoryService categoryService, CurrentUserService currentUserService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.currentUserService = currentUserService;
    }

    public Transaction createTransaction(TransactionRequestDTO obj) {
        Customer cos = customerService.findById(currentUserService.getCurrentUserId());
        Category cat = categoryService.findCategoryById(obj.categoryId(), currentUserService.getCurrentUserId());
        Transaction t = new Transaction();
        t.setCustomer(cos);
        t.setCategory(cat);
        t.setAmount(obj.amount());
        t.setDate(obj.date());
        t.setDescription(obj.description());
        return transactionRepository.save(t);
    }

    public List<Transaction> createInstallmentsTransaction(InstallmentsTransactionRequestDTO obj) {
        UUID groupId = Generators.timeBasedEpochGenerator().generate();
        Customer cos = customerService.findById(currentUserService.getCurrentUserId());
        Category cat = categoryService.findCategoryById(obj.categoryId(), currentUserService.getCurrentUserId());
        BigDecimal installmentAmount = obj.amount().divide(BigDecimal.valueOf(obj.installmentTotal()), 2, RoundingMode.HALF_UP);

        List<Transaction> transactionsToSave = new ArrayList<>();

        for (int i = 1; i <= obj.installmentTotal(); i++) {
            Transaction transaction = new Transaction();
            transaction.setInstallmentGroupId(groupId);
            transaction.setInstallmentNumber(i);
            transaction.setInstallmentTotal(obj.installmentTotal());
            transaction.setCustomer(cos);
            transaction.setCategory(cat);
            transaction.setAmount(installmentAmount);
            transaction.setDescription(obj.description() + " (Installment " + i + " of " + obj.installmentTotal() + ")");
            transaction.setDate(obj.date().plusMonths(i - 1L));
            transactionsToSave.add(transaction);
        }

        return transactionRepository.saveAll(transactionsToSave);
    }

    public void deleteInstallmentsTransaction(UUID groupUUID) {
        if (groupUUID == null) {
            throw new IllegalArgumentException("Installments transaction group id must not be null!");
        }
        List<Transaction> list = transactionRepository.findAllByInstallmentGroupId(groupUUID);
        list.forEach(x -> currentUserService.checkAccess(x.getCustomer().getId()));
        try {
            transactionRepository.deleteByInstallmentGroupId(groupUUID);       
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete transactions for groupUUID: " + groupUUID);
        }
    }

    public Transaction getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(transaction.getCustomer().getId());
        return transaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByCustomerId(currentUserService.getCurrentUserId());
    }

    public List<Transaction> getTransactionsForCustomerBetweenDates(UUID id, LocalDate startDate, LocalDate endDate){
        return transactionRepository.findTransactionsForCustomerBetweenDates(id, startDate, endDate);
    }

    public TransactionSummaryProjection getTransactionSummary(UUID id, LocalDate startDate, LocalDate endDate){
        return transactionRepository.getSummary(id, startDate, endDate);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = getTransactionById(id);
        currentUserService.checkAccess(transaction.getCustomer().getId());

        transaction.setCategory(transactionDetails.getCategory());
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction b = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found!"));
        currentUserService.checkAccess(b.getCustomer().getId());
        try {
            transactionRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete transaction: " + id);
        }
    }
}
