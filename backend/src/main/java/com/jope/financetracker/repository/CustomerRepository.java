package com.jope.financetracker.repository;

import java.util.Optional;
import java.util.UUID;

import com.jope.financetracker.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostumerRepository extends JpaRepository<Customer, UUID>{
    Optional<Customer> findByEmail(String email);
}
