package com.jope.financetracker.dto.customer;

import com.jope.financetracker.enums.Currency;
import com.jope.financetracker.validations.constraints.value_of_enum.ValueOfEnum;

import jakarta.validation.constraints.NotBlank;


public record CustomerRequestDTO(
    @NotBlank(message = "Name is required!")
    String name,
    @NotBlank(message = "Email is required!")
    String email,
    @NotBlank(message = "Password is required!")
    String password,
    @ValueOfEnum(enumClass = Currency.class)
    String currency
) {}
