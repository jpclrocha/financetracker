package com.jope.financetracker.service;

import com.jope.financetracker.dto.budget.BudgetMapper;
import com.jope.financetracker.dto.budget.BudgetRequestDTO;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.repository.BudgetRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CostumerService costumerService;
    private static final BudgetMapper budgetMapper = BudgetMapper.INSTANCE;
    private final CurrentUserService currentUserService;

    public BudgetService(BudgetRepository budgetRepository, CostumerService costumerService, CurrentUserService currentUserService) {
        this.budgetRepository = budgetRepository;
        this.costumerService = costumerService;
        this.currentUserService = currentUserService;
    }

    public Budget createBudget(BudgetRequestDTO budgetRequestDTO) {
        Costumer costumer = costumerService.findById(currentUserService.getCurrentUserId());
        Budget budget = budgetMapper.budgetRequestDTOToBudget(budgetRequestDTO);
        budget.setCostumer(costumer);
        return budgetRepository.save(budget);
    }

    public Budget getBudgetById(Long id) {
        Budget buget = budgetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(buget.getCostumer().getId().equals(currentUserService.getCurrentUserId())){
            return buget;
        }
        throw new AccessDeniedException("This budget does not exist, or you do not have the necessary access rights!");
    }

    public List<Budget> getAllBudgets() {
        currentUserService.hasRole();
        return budgetRepository.findAll();
    }

    public Budget updateBudget(Long id, BudgetRequestDTO budgetRequestDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Costumer costumer = costumerService.findById(currentUserService.getCurrentUserId());

        budget.setCostumer(costumer);
        budget.setName(budgetRequestDTO.name());
        budget.setAmount(budgetRequestDTO.amount());
        budget.setStartPeriod(budgetRequestDTO.startPeriod());
        budget.setEndPeriod(budgetRequestDTO.endPeriod());

        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
