package com.jope.financetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jope.financetracker.model.Category;

public interface CategoryRepository  extends JpaRepository<Category, Long>{
    
}
