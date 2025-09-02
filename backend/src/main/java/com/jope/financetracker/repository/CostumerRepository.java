package com.jope.financetracker.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Costumer;

public interface CostumerRepository extends JpaRepository<Costumer, UUID>{
    
}
