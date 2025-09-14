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
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions().stream()
                .map(transactionMapper::transactionToTransactionResponseDTO).toList());
    }

    @GetMapping("/installments")
    public ResponseEntity<List<InstallmentsTransactionResponseDTO>> getAllInstallmentsTransactions() {
        return ResponseEntity.ok(transactionService.getAllInstallmentsTransaction().stream()
                .map(transactionMapper::installmentTransactionToTransactionResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity
                .ok(transactionMapper.transactionToTransactionResponseDTO(transactionService.getTransactionById(id)));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.status(201).body(transactionMapper.transactionToTransactionResponseDTO(transactionService
                .createTransaction(transactionRequestDTO)));
    }

    @PostMapping("/installment")
    public ResponseEntity<List<InstallmentsTransactionResponseDTO>> createInstallmentsTransaction(
            @RequestBody InstallmentsTransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.status(201).body(transactionService
                .createInstallmentsTransaction(transactionRequestDTO).stream()
                .map(transactionMapper::installmentTransactionToTransactionResponseDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable("id") Long id,
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionMapper.transactionToTransactionResponseDTO(transactionService
                .updateTransaction(id, transactionRequestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/installment/{groupId}")
    public ResponseEntity<Void> deleteInstallmentsTransaction(@PathVariable("groupId") UUID groupId) {
        transactionService.deleteInstallmentsTransaction(groupId);
        return ResponseEntity.noContent().build();
    }
}
