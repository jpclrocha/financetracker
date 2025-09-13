package com.jope.financetracker.dto.dashboard;

import java.math.BigDecimal;

public record CategorySummaryDTO(String name, BigDecimal totalSpent) {
}
