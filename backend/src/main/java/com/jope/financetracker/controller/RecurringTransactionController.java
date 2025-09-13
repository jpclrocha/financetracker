package com.jope.financetracker.controller;

import com.jope.financetracker.dto.recurringtransaction.RecurringTransactionMapper;
import com.jope.financetracker.dto.recurringtransaction.RecurringTransactionRequestDTO;
import com.jope.financetracker.dto.recurringtransaction.RecurringTransactionResponseDTO;
import com.jope.financetracker.service.RecurringTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recurring-transaction")
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;
    private final RecurringTransactionMapper recurringTransactionMapper;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService, RecurringTransactionMapper recurringTransactionMapper) {
        this.recurringTransactionService = recurringTransactionService;
        this.recurringTransactionMapper = recurringTransactionMapper;
    }

    @PostMapping
    public ResponseEntity<RecurringTransactionResponseDTO> createRecurringTransaction(@RequestBody @Valid RecurringTransactionRequestDTO recurringTransactionRequestDTO) {
        return ResponseEntity.status(201).body(recurringTransactionMapper.recurringTransactionToRecurringTransactionResponseDTO(recurringTransactionService.createRecurringTransaction(recurringTransactionRequestDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponseDTO> getRecurringTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(recurringTransactionMapper.recurringTransactionToRecurringTransactionResponseDTO(recurringTransactionService.getRecurringTransactionById(id)));
    }

    @GetMapping
    public ResponseEntity<List<RecurringTransactionResponseDTO>> getAllRecurringTransactions() {
        return ResponseEntity.ok(recurringTransactionService.getAllRecurringTransactions().stream().map(recurringTransactionMapper::recurringTransactionToRecurringTransactionResponseDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponseDTO> updateRecurringTransaction(@PathVariable Long id, @RequestBody RecurringTransactionRequestDTO transactionDetails) {
        return ResponseEntity.ok(recurringTransactionMapper.recurringTransactionToRecurringTransactionResponseDTO(recurringTransactionService.updateRecurringTransaction(id, recurringTransactionMapper.recurringTransactionRequestDTOToRecurringTransaction(transactionDetails))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringTransaction(@PathVariable Long id) {
        recurringTransactionService.deleteRecurringTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
