package com.jope.financetracker.controller;

import com.jope.financetracker.dto.costumer.CostumerMapper;
import com.jope.financetracker.dto.costumer.CostumerRequestDTO;
import com.jope.financetracker.dto.costumer.CostumerResponseDTO;
import com.jope.financetracker.service.CostumerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/costumers")
public class CostumerController {

    private final CostumerService costumerService;
    private static final CostumerMapper costumerMapper = CostumerMapper.INSTANCE;

    public CostumerController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @PostMapping
    public ResponseEntity<CostumerResponseDTO> createCostumer(@RequestBody CostumerRequestDTO costumerRequestDTO) {
        return ResponseEntity.status(201).body(costumerMapper.costumerToCostumerResponseDTO(costumerService.createCostumer(costumerRequestDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CostumerResponseDTO> getCostumerById(@PathVariable UUID id) {
        return ResponseEntity.ok(costumerMapper.costumerToCostumerResponseDTO(costumerService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<CostumerResponseDTO>> getAllCostumers() {
        return ResponseEntity.ok(costumerService.findAll().stream().map(costumerMapper::costumerToCostumerResponseDTO).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CostumerResponseDTO> updateCostumer(@PathVariable UUID id, @RequestBody CostumerRequestDTO costumerRequestDTO) {
        return ResponseEntity.ok(costumerMapper.costumerToCostumerResponseDTO(costumerService.updateCostumer(id, costumerRequestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostumer(@PathVariable UUID id) {
        costumerService.deleteCostumer(id);
        return ResponseEntity.noContent().build();
    }
}
