package com.jope.financetracker.controller;

import com.jope.financetracker.dto.customer.CustomerMapper;
import com.jope.financetracker.dto.customer.CustomerRequestDTO;
import com.jope.financetracker.dto.customer.CustomerResponseDTO;
import com.jope.financetracker.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/costumer")
public class CostumerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CostumerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCostumers() {
        return ResponseEntity.ok(customerService.findAll().stream().map(customerMapper::costumerToCostumerResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCostumerById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(customerMapper.costumerToCostumerResponseDTO(customerService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCostumer(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.status(201).body(customerMapper.costumerToCostumerResponseDTO(customerService.createCostumer(customerRequestDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCostumer(@PathVariable("id") UUID id, @RequestBody CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.ok(customerMapper.costumerToCostumerResponseDTO(customerService.updateCostumer(id, customerRequestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostumer(@PathVariable("id") UUID id) {
        customerService.deleteCostumer(id);
        return ResponseEntity.noContent().build();
    }
}
