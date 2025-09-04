package com.jope.financetracker.service;

import com.jope.financetracker.dto.costumer.CostumerMapper;
import com.jope.financetracker.dto.costumer.CostumerRequestDTO;
import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.CostumerRepository;
import com.jope.financetracker.repository.RoleRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CostumerService {

    private static final CostumerMapper costumerMapper = CostumerMapper.INSTANCE;
    private final CostumerRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CostumerService(CostumerRepository repository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Costumer> findAll() {
        return repository.findAll();
    }

    public Costumer findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Optional<Costumer> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Costumer createCostumer(CostumerRequestDTO costumerRequestDTO) {
        Role r = roleRepository.findByName(Role.Values.BASIC.name());
        Optional<Costumer> opt = repository.findByEmail(costumerRequestDTO.email());

        if(opt.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Costumer costumer = costumerMapper.costumerRequestDTOToCostumer(costumerRequestDTO);
        costumer.setPassword(passwordEncoder.encode(costumerRequestDTO.password()));
        costumer.setRoles(Set.of(r));
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
