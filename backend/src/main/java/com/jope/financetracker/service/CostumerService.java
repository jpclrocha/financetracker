package com.jope.financetracker.service;

import com.jope.financetracker.dto.costumer.CostumerMapper;
import com.jope.financetracker.dto.costumer.CostumerRequestDTO;
import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.CostumerRepository;
import com.jope.financetracker.repository.RoleRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CostumerService {

    private final CostumerMapper costumerMapper;
    private final CostumerRepository repository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public CostumerService(CostumerRepository repository, RoleService roleService, BCryptPasswordEncoder passwordEncoder, CostumerMapper costumerMapper, CurrentUserService currentUserService) {
        this.repository = repository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.costumerMapper = costumerMapper;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("@currentUserService.isAdmin()")
    public List<Costumer> findAll() {
        return repository.findAll();
    }

    public Costumer findById(UUID id) {
        Costumer cos = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(cos.getId());
        return cos;
    }

    public Optional<Costumer> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Costumer createCostumer(CostumerRequestDTO costumerRequestDTO) {
        Role r = roleService.findByName(Role.Values.BASIC.name());
        Optional<Costumer> opt = this.findByEmail(costumerRequestDTO.email());

        if(opt.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Costumer costumer = costumerMapper.costumerRequestDTOToCostumer(costumerRequestDTO);
        costumer.setPassword(passwordEncoder.encode(costumerRequestDTO.password()));
        costumer.setRoles(Set.of(r));
        return repository.save(costumer);
    }

    public Costumer updateCostumer(UUID id, CostumerRequestDTO costumerRequestDTO) {
        currentUserService.checkAccess(id);

        Costumer costumer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        costumer.setName(costumerRequestDTO.name());
        costumer.setEmail(costumerRequestDTO.email());
        costumer.setCurrency(Currency.valueOf(costumerRequestDTO.currency()));

        return repository.save(costumer);
    }

    public void deleteCostumer(UUID id) {
        currentUserService.checkAccess(id);
        try {
            repository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete costumer: " + id);
        }
    }
}
