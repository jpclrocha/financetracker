package com.jope.financetracker.dto.category;

import com.jope.financetracker.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO categoryToCategoryResponseDTO(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isPublic", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "recurringTransactions", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "type", expression = "java(com.jope.financetracker.enums.ExpenseType.valueOf(categoryRequestDTO.type()))")
    Category categoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO);
}
