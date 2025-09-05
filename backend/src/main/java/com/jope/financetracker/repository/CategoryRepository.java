package com.jope.financetracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jope.financetracker.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.isPublic = true OR c.costumer.id = :costumerId")
    List<Category> findAllPublicOrByCostumer(@Param("costumerId") UUID costumerId);
}
