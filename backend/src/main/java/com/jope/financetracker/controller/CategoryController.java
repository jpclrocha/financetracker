package com.jope.financetracker.controller;

import com.jope.financetracker.dto.category.CategoryMapper;
import com.jope.financetracker.dto.category.CategoryRequestDTO;
import com.jope.financetracker.dto.category.CategoryResponseDTO;
import com.jope.financetracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(categoryMapper::categoryToCategoryResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryMapper.categoryToCategoryResponseDTO(categoryService.findCategoryById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO category) {
        return ResponseEntity.status(201).body(categoryMapper.categoryToCategoryResponseDTO(categoryService.createCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequestDTO categoryDetails) {
        return ResponseEntity.ok(categoryMapper.categoryToCategoryResponseDTO(categoryService.updateCategory(id, categoryDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
