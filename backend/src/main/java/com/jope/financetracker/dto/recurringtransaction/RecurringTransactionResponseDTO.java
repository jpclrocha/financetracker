package com.jope.financetracker.dto.recurringtransaction;

import com.jope.financetracker.dto.transaction.TransactionResponseDTO;
import com.jope.financetracker.enums.Frequency;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RecurringTransactionResponseDTO(
    Long id,
    String description,
    BigDecimal amount,
    String categoryName,
    Frequency frequency,
    LocalDate startDate,
    LocalDate nextDueDate,
    Boolean isSubscription,
    List<TransactionResponseDTO> transactions
) {}
