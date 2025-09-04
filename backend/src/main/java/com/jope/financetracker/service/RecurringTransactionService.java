package com.jope.financetracker.service;

import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.RecurringTransaction;
import com.jope.financetracker.repository.RecurringTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;

    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    public RecurringTransaction createRecurringTransaction(RecurringTransaction recurringTransaction) {
        return recurringTransactionRepository.save(recurringTransaction);
    }

    public RecurringTransaction getRecurringTransactionById(Long id) {
        return recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<RecurringTransaction> getAllRecurringTransactions() {
        return recurringTransactionRepository.findAll();
    }

    public RecurringTransaction updateRecurringTransaction(Long id, RecurringTransaction transactionDetails) {
        RecurringTransaction recurringTransaction = getRecurringTransactionById(id);

        recurringTransaction.setCostumer(transactionDetails.getCostumer());
        recurringTransaction.setDescription(transactionDetails.getDescription());
        recurringTransaction.setAmount(transactionDetails.getAmount());
        recurringTransaction.setCategory(transactionDetails.getCategory());
        recurringTransaction.setFrequency(transactionDetails.getFrequency());
        recurringTransaction.setStartDate(transactionDetails.getStartDate());
        recurringTransaction.setNextDueDate(transactionDetails.getNextDueDate());
        recurringTransaction.setIsSubscripion(transactionDetails.getIsSubscripion());

        return recurringTransactionRepository.save(recurringTransaction);
    }

    public void deleteRecurringTransaction(Long id) {
        recurringTransactionRepository.deleteById(id);
    }
}
