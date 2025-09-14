package com.jope.financetracker.dto.customer;

import java.util.UUID;

public record CustomerResponseDTO(
    UUID id,
    String name,
    String email,
    String currency
) {}
