package com.jope.financetracker.controller;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RestController;

import com.jope.financetracker.dto.login.LoginRequestDTO;
import com.jope.financetracker.dto.login.LoginResponseDTO;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.service.CostumerService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final CostumerService costumerService;
    private final PasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, CostumerService costumerService, PasswordEncoder passwordEncoder){
        this.jwtEncoder = jwtEncoder;
        this.costumerService = costumerService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> postMethodName(@RequestBody LoginRequestDTO obj) {
        Optional<Costumer> opt = costumerService.findByEmail(obj.email());

        if(opt.isEmpty() || !opt.get().isLoginCorrect(obj, passwordEncoder)){
            throw new BadCredentialsException("Username or password is invalid!");
        }

        var scopes = opt.get().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

        Instant now = Instant.now();
        long expiresIn = 300L;

        var claims = JwtClaimsSet
            .builder()
            .issuer("financetracker_backend")
            .subject(opt.get().getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("scope", scopes)
            .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok().body(new LoginResponseDTO(jwtValue, expiresIn));
    }
    
}
