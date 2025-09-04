package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.enums.Frequency;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RecurringTransactionRequestDTO(
    UUID costumerId,
    String description,
    BigDecimal amount,
    Long categoryId,
    Frequency frequency,
    LocalDate startDate,
    LocalDate nextDueDate,
    Boolean isSubscripion
) {}
