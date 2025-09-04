package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.enums.Frequency;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurringTransactionResponseDTO(
    Long id,
    String description,
    BigDecimal amount,
    String categoryName,
    Frequency frequency,
    LocalDate startDate,
    LocalDate nextDueDate,
    Boolean isSubscripion
) {}
