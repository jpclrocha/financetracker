package com.jope.financetracker.service;

import com.jope.financetracker.dto.customer.CustomerMapper;
import com.jope.financetracker.dto.customer.CustomerRequestDTO;
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

import java.util.*;

@Service
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository repository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    private final TokenService tokenService;

    public CustomerService(CustomerMapper customerMapper, CustomerRepository repository,
                           RoleService roleService, BCryptPasswordEncoder passwordEncoder,
                           CurrentUserService currentUserService, TokenService tokenService) {
        this.customerMapper = customerMapper;
        this.repository = repository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
        this.tokenService = tokenService;
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

    public Customer createCostumer(CustomerRequestDTO customerRequestDTO) {
        Role r = roleService.findByName(Role.Values.BASIC.name());
        Optional<Customer> opt = this.findByEmail(customerRequestDTO.email());

        if(opt.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Customer customer = customerMapper.costumerRequestDTOToCostumer(customerRequestDTO);
        customer.setPassword(passwordEncoder.encode(customerRequestDTO.password()));
        customer.setRoles(Set.of(r));
        return repository.save(customer);
    }

    public Customer updateCostumer(UUID id, CustomerRequestDTO customerRequestDTO) {
        currentUserService.checkAccess(id);

        Customer customer = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(!Objects.equals(customer.getEmail(), customerRequestDTO.email()) && repository.existsByEmail(customerRequestDTO.email())){
            throw new DatabaseException("This email is already in use!");
        }

        customer.setName(customerRequestDTO.name());
        customer.setEmail(customerRequestDTO.email());
        customer.setCurrency(Currency.valueOf(customerRequestDTO.currency()));

        return repository.save(customer);
    }

    public void deleteCostumer(UUID id) {
        currentUserService.checkAccess(id);
        try {
            tokenService.revokeAllRefreshTokensForUser(id);
            repository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Failed to delete costumer: " + id);
        }
    }
}
