package com.jope.financetracker.controller;

import com.jope.financetracker.dto.transaction.InstallmentsTransactionRequestDTO;
import com.jope.financetracker.dto.transaction.InstallmentsTransactionResponseDTO;
import com.jope.financetracker.dto.transaction.TransactionMapper;
import com.jope.financetracker.dto.transaction.TransactionRequestDTO;
import com.jope.financetracker.dto.transaction.TransactionResponseDTO;
import com.jope.financetracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.status(201).body(transactionMapper.transactionToTransactionResponseDTO(transactionService
                .createTransaction(transactionRequestDTO)));
    }

    @PostMapping("/installments")
    public ResponseEntity<List<InstallmentsTransactionResponseDTO>> createInstallmentsTransaction(
            @RequestBody InstallmentsTransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.status(201).body(transactionService
                .createInstallmentsTransaction(transactionRequestDTO).stream()
                .map(transactionMapper::installmentTransactionToTransactionResponseDTO).toList());
    }

    @DeleteMapping("/installments/{groupId}")
    public ResponseEntity<Void> deleteInstallmentsTransaction(@PathVariable UUID groupId) {
        transactionService.deleteInstallmentsTransaction(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity
                .ok(transactionMapper.transactionToTransactionResponseDTO(transactionService.getTransactionById(id)));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions().stream()
                .map(transactionMapper::transactionToTransactionResponseDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable Long id,
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionMapper.transactionToTransactionResponseDTO(transactionService
                .updateTransaction(id, transactionMapper.transactionRequestDTOToTransaction(transactionRequestDTO))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
