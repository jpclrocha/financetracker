package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.enums.Frequency;
import com.jope.financetracker.validations.constraints.value_of_enum.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RecurringTransactionUpdateDTO(
        String description,
        @PositiveOrZero(message = "The amount cannot be negative")
        BigDecimal amount,
        Long categoryId,
        @ValueOfEnum(enumClass = Frequency.class, message = "Value not valid for Frequency!")
        String frequency,
        @NotNull(message = "Is subscription field is required!")
        boolean isSubscription) {
 }