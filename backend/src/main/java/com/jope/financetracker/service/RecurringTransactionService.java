package com.jope.financetracker.service;

import com.jope.financetracker.dto.recurringtransaction.RecurringTransactionRequestDTO;
import com.jope.financetracker.enums.Frequency;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.RecurringTransaction;
import com.jope.financetracker.repository.RecurringTransactionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final CurrentUserService currentUserService;
    private final CustomerService customerService;
    private final CategoryService categoryService;


    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository, CurrentUserService currentUserService, CustomerService customerService, CategoryService categoryService) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.currentUserService = currentUserService;
        this.customerService = customerService;
        this.categoryService = categoryService;
    }

    public RecurringTransaction createRecurringTransaction(RecurringTransactionRequestDTO obj) {
        Customer cos = customerService.findById(currentUserService.getCurrentUserId());
        Category category = categoryService.findCategoryById(obj.categoryId(), currentUserService.getCurrentUserId());
        RecurringTransaction recurringTransaction = new RecurringTransaction();
        recurringTransaction.setCustomer(cos);
        recurringTransaction.setAmount(obj.amount());
        recurringTransaction.setDescription(obj.description());
        recurringTransaction.setFrequency(Frequency.valueOf(obj.frequency()));
        recurringTransaction.setIsSubscripion(obj.isSubscripion());
        recurringTransaction.setStartDate(obj.startDate());
        recurringTransaction.setNextDueDate(obj.nextDueDate());
        recurringTransaction.setCategory(category);
        return recurringTransactionRepository.save(recurringTransaction);
    }

    public RecurringTransaction getRecurringTransactionById(Long id) {
        RecurringTransaction rec = recurringTransactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(rec.getCustomer().getId());
        return rec;
    }

    public List<RecurringTransaction> getAllRecurringTransactions() {
        return recurringTransactionRepository.findAllByCustomerId(currentUserService.getCurrentUserId());
    }

    public RecurringTransaction updateRecurringTransaction(Long id, RecurringTransaction transactionDetails) {
        RecurringTransaction recurringTransaction = getRecurringTransactionById(id);

        currentUserService.checkAccess(recurringTransaction.getCustomer().getId());
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
        RecurringTransaction b = recurringTransactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurring transaction not found!"));
        currentUserService.checkAccess(b.getCustomer().getId());
        try {
            recurringTransactionRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete recurring transaction: " + id);
        }
    }
}
