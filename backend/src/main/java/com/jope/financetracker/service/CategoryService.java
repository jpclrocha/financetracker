package com.jope.financetracker.service;

import com.jope.financetracker.dto.category.CategoryMapper;
import com.jope.financetracker.dto.category.CategoryRequestDTO;
import com.jope.financetracker.enums.ExpenseType;
import com.jope.financetracker.exceptions.ResourceNotFoundException;
import com.jope.financetracker.model.Category;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CostumerService costumerService;
    private static final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    public CategoryService(CategoryRepository categoryRepository, CostumerService costumerService) {
        this.categoryRepository = categoryRepository;
        this.costumerService = costumerService;
    }

    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        Costumer costumer = costumerService.findById(categoryRequestDTO.costumerId());
        Category category = categoryMapper.categoryRequestDTOToCategory(categoryRequestDTO);
        category.setCostumer(costumer);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Costumer costumer = costumerService.findById(categoryRequestDTO.costumerId());

        category.setCostumer(costumer);
        category.setName(categoryRequestDTO.name());
        category.setType(ExpenseType.valueOf(categoryRequestDTO.type()));

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
