package com.jope.financetracker.service;

import com.jope.financetracker.dto.budget.BudgetMapper;
import com.jope.financetracker.dto.budget.BudgetRequestDTO;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.repository.BudgetRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CustomerService customerService;
    private final BudgetMapper budgetMapper;
    private final CurrentUserService currentUserService;
    private final CategoryService categoryService;

    public BudgetService(BudgetRepository budgetRepository, CustomerService customerService,
                         BudgetMapper budgetMapper, CurrentUserService currentUserService, CategoryService categoryService) {
        this.budgetRepository = budgetRepository;
        this.customerService = customerService;
        this.budgetMapper = budgetMapper;
        this.currentUserService = currentUserService;
        this.categoryService = categoryService;
    }

    public Budget createBudget(BudgetRequestDTO budgetRequestDTO) {
        Customer customer = customerService.findById(currentUserService.getCurrentUserId());
        Budget budget = budgetMapper.budgetRequestDTOToBudget(budgetRequestDTO);
        budget.setCustomer(customer);
        return budgetRepository.save(budget);
    }

    public Budget getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(budget.getCustomer().getId());
        return budget;
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAllByCustomerId(currentUserService.getCurrentUserId());
    }

    public Budget updateBudget(Long id, BudgetRequestDTO budgetRequestDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        currentUserService.checkAccess(budget.getCustomer().getId());

        budget.setName(budgetRequestDTO.name());
        budget.setAmount(budgetRequestDTO.amount());
        budget.setStartPeriod(budgetRequestDTO.startPeriod());
        budget.setEndPeriod(budgetRequestDTO.endPeriod());

        return budgetRepository.save(budget);
    }

    public Budget addCategoryToBudget(Long budgetId,Long categoryId){
        Budget budget = this.getBudgetById(budgetId);
        currentUserService.checkAccess(budget.getCustomer().getId());

        Category cat = categoryService.findCategoryById(categoryId);
        currentUserService.checkAccess(cat.getCustomer().getId());

        budget.addCategory(cat);

        return budgetRepository.save(budget);
    }

    public void removeCategoryFromBudget(Long budgetId, Long categoryId){
        Budget budget = this.getBudgetById(budgetId);
        currentUserService.checkAccess(budget.getCustomer().getId());

        Category cat = categoryService.findCategoryById(categoryId);
        currentUserService.checkAccess(cat.getCustomer().getId());

        budget.removeCategory(cat);

        budgetRepository.save(budget);
    }

//    public void unlinkCategory(Long categoryId){
//        budgetRepository.setCategoryToNull(categoryId);
//    }

    public void deleteBudget(Long id) {
        Budget b = budgetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Budget not found!"));
        currentUserService.checkAccess(b.getCustomer().getId());
        try {
            budgetRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete budget: " + id);
        }
    }
}
