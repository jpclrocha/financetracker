package com.jope.financetracker.controller;

import com.jope.financetracker.dto.costumer.CostumerMapper;
import com.jope.financetracker.dto.costumer.CostumerRequestDTO;
import com.jope.financetracker.dto.costumer.CostumerResponseDTO;
import com.jope.financetracker.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/costumer")
public class CostumerController {

    private final CustomerService customerService;
    private final CostumerMapper costumerMapper;

    public CostumerController(CustomerService customerService, CostumerMapper costumerMapper) {
        this.customerService = customerService;
        this.costumerMapper = costumerMapper;
    }

    @PostMapping
    public ResponseEntity<CostumerResponseDTO> createCostumer(@RequestBody @Valid CostumerRequestDTO costumerRequestDTO) {
        return ResponseEntity.status(201).body(costumerMapper.costumerToCostumerResponseDTO(customerService.createCostumer(costumerRequestDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostumerResponseDTO> getCostumerById(@PathVariable UUID id, JwtAuthenticationToken token) {
        if(id.equals(UUID.fromString(token.getName()))){
            return ResponseEntity.ok(costumerMapper.costumerToCostumerResponseDTO(customerService.findById(id)));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<CostumerResponseDTO>> getAllCostumers() {
        return ResponseEntity.ok(customerService.findAll().stream().map(costumerMapper::costumerToCostumerResponseDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostumerResponseDTO> updateCostumer(@PathVariable UUID id, @RequestBody CostumerRequestDTO costumerRequestDTO, JwtAuthenticationToken token) {
        if(id.equals(UUID.fromString(token.getName()))){
            return ResponseEntity.ok(costumerMapper.costumerToCostumerResponseDTO(customerService.updateCostumer(id, costumerRequestDTO)));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteCostumer(@PathVariable UUID id) {
        customerService.deleteCostumer(id);
        return ResponseEntity.noContent().build();
    }
}
