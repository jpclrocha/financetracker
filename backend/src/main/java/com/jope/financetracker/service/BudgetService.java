package com.jope.financetracker.service;

import com.jope.financetracker.dto.budget.BudgetMapper;
import com.jope.financetracker.dto.budget.BudgetRequestDTO;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CostumerService costumerService;
    private static final BudgetMapper budgetMapper = BudgetMapper.INSTANCE;

    public BudgetService(BudgetRepository budgetRepository, CostumerService costumerService) {
        this.budgetRepository = budgetRepository;
        this.costumerService = costumerService;
    }

    public Budget createBudget(BudgetRequestDTO budgetRequestDTO) {
        Costumer costumer = costumerService.findById(budgetRequestDTO.costumerId());
        Budget budget = budgetMapper.budgetRequestDTOToBudget(budgetRequestDTO);
        budget.setCostumer(costumer);
        return budgetRepository.save(budget);
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget updateBudget(Long id, BudgetRequestDTO budgetRequestDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Costumer costumer = costumerService.findById(budgetRequestDTO.costumerId());

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
