package com.jope.financetracker.dto.costumer;

import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.validations.constraints.value_of_enum.ValueOfEnum;

public record CostumerRequestDTO(
    String name,
    String email,
    @ValueOfEnum(enumClass = Currency.class)
    String currency
) {}
