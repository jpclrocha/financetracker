package com.jope.financetracker.service;

import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository){
        this.repository = repository;
    }


    public Role findByName(String name){
        return repository.findByName(name);
    }
}
