package com.jope.financetracker.service;

import com.jope.financetracker.dto.costumer.CostumerMapper;
import com.jope.financetracker.dto.costumer.CostumerRequestDTO;
import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.repository.CostumerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CostumerService {

    private static final CostumerMapper costumerMapper = CostumerMapper.INSTANCE;
    private final CostumerRepository repository;

    public CostumerService(CostumerRepository repository) {
        this.repository = repository;
    }

    public List<Costumer> findAll() {
        return repository.findAll();
    }

    public Costumer findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Costumer createCostumer(CostumerRequestDTO costumerRequestDTO) {
        Costumer costumer = costumerMapper.costumerRequestDTOToCostumer(costumerRequestDTO);
        return repository.save(costumer);
    }

    public Costumer updateCostumer(UUID id, CostumerRequestDTO costumerRequestDTO) {
        Costumer costumer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        costumer.setName(costumerRequestDTO.name());
        costumer.setEmail(costumerRequestDTO.email());
        costumer.setCurrency(Currency.valueOf(costumerRequestDTO.currency()));

        return repository.save(costumer);
    }

    public void deleteCostumer(UUID id) {
        repository.deleteById(id);
    }
}
