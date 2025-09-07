package com.jope.financetracker.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(@NotBlank(message = "Refresh token should not be empty!") String refreshToken) {
}
