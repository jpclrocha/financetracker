package com.jope.financetracker.dto.costumer;

import java.util.UUID;

public record CostumerResponseDTO(
    UUID id,
    String name,
    String email,
    String currency
) {}
