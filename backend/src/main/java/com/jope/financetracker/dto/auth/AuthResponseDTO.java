package com.jope.financetracker.dto.auth;

import com.jope.financetracker.dto.customer.CustomerResponseDTO;

public record AuthResponseDTO(String accessToken, String refreshToken, CustomerResponseDTO customer) {
}
