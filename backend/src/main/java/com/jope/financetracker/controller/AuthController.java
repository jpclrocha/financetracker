package com.jope.financetracker.controller;

import com.jope.financetracker.dto.auth.RefreshTokenRequestDTO;
import com.jope.financetracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jope.financetracker.dto.auth.AuthRequestDTO;
import com.jope.financetracker.dto.auth.AuthResponseDTO;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService authService){
        this.service = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO obj) {
        return ResponseEntity.ok().body(service.login(obj));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> renewTokens(@RequestBody @Valid RefreshTokenRequestDTO obj) {
        return ResponseEntity.ok().body(service.renewToken(obj.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDTO obj) {
        service.logout(obj.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
