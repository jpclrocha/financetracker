package com.jope.financetracker.dto.category;

import com.jope.financetracker.enums.ExpenseType;
import com.jope.financetracker.validations.constraints.value_of_enum.ValueOfEnum;

import java.util.UUID;

public record CategoryRequestDTO(
    UUID costumerId,
    String name,
    @ValueOfEnum(enumClass = ExpenseType.class)
    String type
) {}
