package com.jope.financetracker.service;

import com.jope.financetracker.dto.recurringtransaction.RecurringTransactionRequestDTO;
import com.jope.financetracker.dto.recurringtransaction.RecurringTransactionUpdateDTO;
import com.jope.financetracker.dto.transaction.TransactionRequestDTO;
import com.jope.financetracker.enums.Frequency;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.RecurringTransaction;
import com.jope.financetracker.model.Transaction;
import com.jope.financetracker.repository.RecurringTransactionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final CurrentUserService currentUserService;
    private final CustomerService customerService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;


    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository, CurrentUserService currentUserService,
                                       CustomerService customerService, CategoryService categoryService, TransactionService transactionService) {
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.currentUserService = currentUserService;
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    @Transactional
    public RecurringTransaction createRecurringTransaction(RecurringTransactionRequestDTO obj) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = obj.startDate();

        Customer cos = customerService.findById(currentUserService.getCurrentUserId());
        Category category = null;
        if(obj.categoryId() != null){
            category = categoryService.findCategoryById(obj.categoryId());
        }
        RecurringTransaction rt = new RecurringTransaction();
        rt.setCustomer(cos);
        rt.setAmount(obj.amount());
        rt.setDescription(obj.description());
        rt.setFrequency(Frequency.valueOf(obj.frequency()));
        rt.setIsSubscription(obj.isSubscription());
        rt.setStartDate(startDate);
        rt.setCategory(category);

        if(!startDate.isAfter(today)){
            LocalDate cursorDate = startDate;
            while(!cursorDate.isAfter(today)){
                transactionService.createRecurringTransaction(new TransactionRequestDTO(
                        obj.categoryId(),
                        obj.amount(),
                        cursorDate,
                        obj.description()
                ), rt);
                cursorDate = updateNextDueDate(cursorDate, rt.getFrequency());
            }
            rt.setNextDueDate(cursorDate);
        }else{
            rt.setNextDueDate(startDate);
        }
        return recurringTransactionRepository.save(rt);
    }

    public RecurringTransaction getRecurringTransactionById(Long id) {
        RecurringTransaction rec = recurringTransactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(rec.getCustomer().getId());
        return rec;
    }

    public List<RecurringTransaction> getAllRecurringTransactions() {
        return recurringTransactionRepository.findAllByCustomerId(currentUserService.getCurrentUserId());
    }

    @Transactional
    public RecurringTransaction updateRecurringTransaction(Long id, RecurringTransactionUpdateDTO details) {
        RecurringTransaction rt = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecurringTransaction not found with id: " + id));

        currentUserService.checkAccess(rt.getCustomer().getId());

        Category category = null;
        if(details.categoryId() != null){
            category = categoryService.findCategoryById(details.categoryId());
        }

        rt.setDescription(details.description());
        rt.setAmount(details.amount());
        rt.setCategory(category);
        rt.setIsSubscription(details.isSubscription());

        boolean frequencyChanged = !rt.getFrequency().equals(Frequency.valueOf(details.frequency()));

        if (frequencyChanged) {
            rt.setFrequency(Frequency.valueOf(details.frequency()));

            // Recalculate the next due date based on the *last created transaction*.
            // This requires a method to find the most recent transaction instance.
            // Fallback to start date if no transactions exist yet
            LocalDate newNextDueDate = transactionService.findMostRecentTransactionDateByRecurringId(id).orElse(rt.getStartDate());
            // Keep advancing the date until it's in the future
            while (!newNextDueDate.isAfter(LocalDate.now())) {
                newNextDueDate = updateNextDueDate(newNextDueDate, rt.getFrequency());
            }
            rt.setNextDueDate(newNextDueDate);
        }
        return recurringTransactionRepository.save(rt);
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

    @Transactional
    public void processDueTransactions() {
        LocalDate today = LocalDate.now();
        List<RecurringTransaction> dueTransactions = recurringTransactionRepository.findByNextDueDateAndIsActiveTrue(today);

        for (RecurringTransaction rt : dueTransactions) {
            transactionService.createTransaction(new TransactionRequestDTO(
                    rt.getCategory().getId(),
                    rt.getAmount(),
                    today,
                    rt.getDescription()
            ));
            rt.setNextDueDate(updateNextDueDate(rt.getStartDate(), rt.getFrequency()));
            recurringTransactionRepository.save(rt);
        }
    }

    private LocalDate updateNextDueDate(LocalDate fromDate, Frequency frequency) {
        return switch (frequency) {
            case DAILY -> fromDate.plusDays(1);
            case WEEKLY -> fromDate.plusWeeks(1);
            case MONTHLY -> fromDate.plusMonths(1);
            case YEARLY -> fromDate.plusYears(1);
        };
    }
}
