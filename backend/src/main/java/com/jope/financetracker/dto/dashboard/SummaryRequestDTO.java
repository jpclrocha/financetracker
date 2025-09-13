package com.jope.financetracker.dto.dashboard;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record SummaryRequestDTO(
        @NotNull(message = "Start date must no be null!")
        LocalDate startDate,
        @Nullable
        LocalDate endDate) {
}
