package com.jope.financetracker.controller;

import com.jope.financetracker.dto.customer.CustomerResponseDTO;
import com.jope.financetracker.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jope.financetracker.dto.auth.AuthRequestDTO;
import com.jope.financetracker.dto.auth.AuthResponseDTO;

import java.time.Duration;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;
    @Value("${jwt.cookie.secure}")
    private Boolean secureCookie;

    private final static String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private final static String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private final static Boolean HTTP_ONLY_COOKIE = true;
    private final static String COOKIE_PATH = "/";
    private final static String SAME_SITE_COOKIE = "Strict";

    private final AuthService service;

    public AuthController(AuthService authService){
        this.service = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerResponseDTO> login(@RequestBody AuthRequestDTO obj, HttpServletResponse response) {
        AuthResponseDTO t = service.login(obj);
        ResponseCookie accessCookie = createTokenCookie(ACCESS_TOKEN_COOKIE_NAME, t.accessToken(), accessTokenExpiration);
        ResponseCookie refreshCookie = createTokenCookie(REFRESH_TOKEN_COOKIE_NAME, t.refreshToken(), refreshTokenExpiration);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(t.customer());
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> renewTokens(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
        AuthResponseDTO obj = service.renewToken(refreshToken);
        ResponseCookie newAccessToken = createTokenCookie(ACCESS_TOKEN_COOKIE_NAME, obj.accessToken(), accessTokenExpiration);
        ResponseCookie newRefreshToken = createTokenCookie(REFRESH_TOKEN_COOKIE_NAME, obj.refreshToken(), refreshTokenExpiration);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.SET_COOKIE, newAccessToken.toString())
                .header(HttpHeaders.SET_COOKIE, newRefreshToken.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
        service.logout(refreshToken);

        ResponseCookie clearAccessToken = createExpiredTokenCookie(ACCESS_TOKEN_COOKIE_NAME);
        ResponseCookie clearRefreshToken = createExpiredTokenCookie(REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.SET_COOKIE, clearAccessToken.toString())
                .header(HttpHeaders.SET_COOKIE, clearRefreshToken.toString())
                .build();
    }

    private ResponseCookie createTokenCookie(String tokenName, String tokenValue, Long expiration) {
        Duration maxAge = Duration.ofMillis(expiration);
        return ResponseCookie.from(tokenName, tokenValue)
                .httpOnly(HTTP_ONLY_COOKIE)
                .secure(secureCookie)
                .path(COOKIE_PATH)
                .maxAge(maxAge)
                .sameSite(SAME_SITE_COOKIE)
                .build();
    }

    private ResponseCookie createExpiredTokenCookie(String tokenName) {
        return ResponseCookie.from(tokenName, "")
                .httpOnly(HTTP_ONLY_COOKIE)
                .secure(secureCookie)
                .path(COOKIE_PATH)
                .maxAge(0)
                .sameSite(SAME_SITE_COOKIE)
                .build();
    }
}
