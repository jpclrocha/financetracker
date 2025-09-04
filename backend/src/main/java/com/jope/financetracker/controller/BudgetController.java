package com.jope.financetracker.controller;

import com.jope.financetracker.dto.budget.BudgetMapper;
import com.jope.financetracker.dto.budget.BudgetRequestDTO;
import com.jope.financetracker.dto.budget.BudgetResponseDTO;
import com.jope.financetracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetMapper budgetMapper;

    public BudgetController(BudgetService budgetService, BudgetMapper budgetMapper) {
        this.budgetService = budgetService;
        this.budgetMapper = budgetMapper;
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> createBudget(@RequestBody BudgetRequestDTO budget) {
        return ResponseEntity.status(201).body(budgetMapper
                .budgetToBudgetResponseDTO(budgetService.createBudget(budget)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> getBudgetById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetMapper.budgetToBudgetResponseDTO(budgetService.getBudgetById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<BudgetResponseDTO>> getAllBudgets() {
        return ResponseEntity
                .ok(budgetService.getAllBudgets().stream().map(budgetMapper::budgetToBudgetResponseDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> updateBudget(@PathVariable Long id,
            @RequestBody BudgetRequestDTO budgetDetails) {
        return ResponseEntity.ok(budgetMapper.budgetToBudgetResponseDTO(
                budgetService.updateBudget(id, budgetDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
