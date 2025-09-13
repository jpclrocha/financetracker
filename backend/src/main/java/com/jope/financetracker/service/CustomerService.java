package com.jope.financetracker.service;

import com.jope.financetracker.dto.costumer.CostumerMapper;
import com.jope.financetracker.dto.costumer.CostumerRequestDTO;
import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.CustomerRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomerService {

    private final CostumerMapper costumerMapper;
    private final CustomerRepository repository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public CustomerService(CustomerRepository repository, RoleService roleService, BCryptPasswordEncoder passwordEncoder, CostumerMapper costumerMapper, CurrentUserService currentUserService) {
        this.repository = repository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.costumerMapper = costumerMapper;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("@currentUserService.isAdmin()")
    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer findById(UUID id) {
        Customer cos = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(cos.getId());
        return cos;
    }

    public Optional<Customer> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Customer createCostumer(CostumerRequestDTO costumerRequestDTO) {
        Role r = roleService.findByName(Role.Values.BASIC.name());
        Optional<Customer> opt = this.findByEmail(costumerRequestDTO.email());

        if(opt.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Customer customer = costumerMapper.costumerRequestDTOToCostumer(costumerRequestDTO);
        customer.setPassword(passwordEncoder.encode(costumerRequestDTO.password()));
        customer.setRoles(Set.of(r));
        return repository.save(customer);
    }

    public Customer updateCostumer(UUID id, CostumerRequestDTO costumerRequestDTO) {
        currentUserService.checkAccess(id);

        Customer customer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        customer.setName(costumerRequestDTO.name());
        customer.setEmail(costumerRequestDTO.email());
        customer.setCurrency(Currency.valueOf(costumerRequestDTO.currency()));

        return repository.save(customer);
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
