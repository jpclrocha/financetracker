package com.jope.financetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Role;

public interface RoleRepository  extends JpaRepository<Role, Long>{
    Role findByName(String name);
}
