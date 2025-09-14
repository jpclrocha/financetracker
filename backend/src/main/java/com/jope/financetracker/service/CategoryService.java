package com.jope.financetracker.service;

import com.jope.financetracker.dto.category.CategoryMapper;
import com.jope.financetracker.dto.category.CategoryRequestDTO;
import com.jope.financetracker.enums.ExpenseType;
import com.jope.financetracker.exceptions.DatabaseException;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Budget;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.Transaction;
import com.jope.financetracker.repository.CategoryRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CustomerService customerService;
    private final CategoryMapper categoryMapper;
    private final CurrentUserService currentUserService;

    public CategoryService(CategoryRepository categoryRepository, CustomerService customerService,
                           CategoryMapper categoryMapper, CurrentUserService currentUserService) {
        this.categoryRepository = categoryRepository;
        this.customerService = customerService;
        this.categoryMapper = categoryMapper;
        this.currentUserService = currentUserService;
    }

    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        Customer customer = customerService.findById(currentUserService.getCurrentUserId());
        Category category = categoryMapper.categoryRequestDTOToCategory(categoryRequestDTO);

        category.setCustomer(customer);
        category.setIsPublic(false);
        return categoryRepository.save(category);
    }

    @PreAuthorize("@currentUserService.isAdmin()")
    public Category createPublicCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryMapper.categoryRequestDTOToCategory(categoryRequestDTO);
        category.setCustomer(null);
        category.setIsPublic(true);
        return categoryRepository.save(category);
    }

    public Category findCategoryById(Long id) {
        Category c = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        UUID customerId = currentUserService.getCurrentUserId();

        if (Boolean.FALSE.equals(c.getIsPublic()) && (!c.getCustomer().getId().equals(customerId))) {
            throw new AccessDeniedException("This category does not exists or the customer does not own this category!");
        }

        return c;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllPublicOrByCustomer(currentUserService.getCurrentUserId());
    }

    public Category updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        currentUserService.checkAccess(category.getCustomer().getId());

        category.setName(categoryRequestDTO.name());
        category.setType(ExpenseType.valueOf(categoryRequestDTO.type()));

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        currentUserService.checkAccess(category.getCustomer().getId());
        try {
            for (Transaction transaction : category.getTransactions()){
                transaction.setCategory(null);
            }
            for (Budget budget : category.getBudgets()){
                budget.removeCategory(category);
            }
            categoryRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to delete category: " + id);
        }
    }
}
