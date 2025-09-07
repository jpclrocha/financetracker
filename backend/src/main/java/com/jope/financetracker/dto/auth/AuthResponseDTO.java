package com.jope.financetracker.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponseDTO(
        @JsonProperty(value = "access_token")
        String accessToken,
        @JsonProperty(value = "refresh_token")
        String refreshToken,
        @JsonProperty(value = "expires_in")
        Long expiresIn) {
    
}
