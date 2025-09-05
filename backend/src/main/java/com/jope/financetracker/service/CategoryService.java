package com.jope.financetracker.service;

import com.jope.financetracker.dto.category.CategoryMapper;
import com.jope.financetracker.dto.category.CategoryRequestDTO;
import com.jope.financetracker.enums.ExpenseType;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.repository.CategoryRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CostumerService costumerService;
    private final CategoryMapper categoryMapper;
    private final CurrentUserService currentUserService;

    public CategoryService(CategoryRepository categoryRepository, CostumerService costumerService,
            CategoryMapper categoryMapper, CurrentUserService currentUserService) {
        this.categoryRepository = categoryRepository;
        this.costumerService = costumerService;
        this.categoryMapper = categoryMapper;
        this.currentUserService = currentUserService;
    }

    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        Costumer costumer = costumerService.findById(currentUserService.getCurrentUserId());
        Category category = categoryMapper.categoryRequestDTOToCategory(categoryRequestDTO);
        category.setCostumer(costumer);
        category.setIsPublic(false);
        return categoryRepository.save(category);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public Category createPublicCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryMapper.categoryRequestDTOToCategory(categoryRequestDTO);
        category.setCostumer(null);
        category.setIsPublic(true);
        return categoryRepository.save(category);
    }

    public Category findCategoryById(Long id, @Nullable UUID costumerId) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (Boolean.FALSE.equals(c.getIsPublic())
                && (costumerId == null || !c.getCostumer().getId().equals(costumerId))) {
            throw new AccessDeniedException("This costumer does not own this category!");
        }

        return c;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllPublicOrByCostumer(currentUserService.getCurrentUserId());
    }

    public Category updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        if(!category.getCostumer().getId().equals(currentUserService.getCurrentUserId())){
            throw new AccessDeniedException("The category does not exist, or the user does not have the necessary access rights!");
        }

        category.setName(categoryRequestDTO.name());
        category.setType(ExpenseType.valueOf(categoryRequestDTO.type()));

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(!category.getCostumer().getId().equals(currentUserService.getCurrentUserId())){
            throw new AccessDeniedException("The category does not exist, or the user does not have the necessary access rights!");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to delete category: " + id);
        }
    }
}
