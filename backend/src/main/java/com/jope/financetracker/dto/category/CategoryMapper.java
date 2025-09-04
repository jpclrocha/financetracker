package com.jope.financetracker.dto.category;

import com.jope.financetracker.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponseDTO categoryToCategoryResponseDTO(Category category);

    @Mapping(source = "costumerId", target = "costumer.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isPublic", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "recurringTransactions", ignore = true)
    Category categoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO);
}
