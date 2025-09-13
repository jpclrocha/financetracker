package com.jope.financetracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jope.financetracker.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.isPublic = true OR c.customer.id = :customerId")
    List<Category> findAllPublicOrByCustomer(@Param("customerId") UUID costumerId);

    boolean existsByNameAndIsPublic(String name, boolean isPublic);
}
