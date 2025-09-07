package com.jope.financetracker.service;

import com.fasterxml.uuid.Generators;
import com.jope.financetracker.dto.auth.AuthRequestDTO;
import com.jope.financetracker.dto.auth.AuthResponseDTO;
import com.jope.financetracker.exceptions.InvalidTokenException;
import com.jope.financetracker.model.Costumer;
import com.jope.financetracker.model.RefreshToken;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {


    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final CostumerService costumerService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(TokenService tokenService, CostumerService costumerService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.costumerService = costumerService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO login(AuthRequestDTO obj){
        Optional<Costumer> opt = costumerService.findByEmail(obj.email());

        if(opt.isEmpty() || !opt.get().isLoginCorrect(obj, passwordEncoder)){
            throw new BadCredentialsException("Username or password is invalid!");
        }

        Costumer cos = opt.get();
        String accessToken = tokenService.createToken(cos);
        tokenService.revokeAllTokensForUser(cos.getId());
        String refreshToken = tokenService.createRefreshToken(cos).getToken();

        return new AuthResponseDTO(accessToken, refreshToken, accessTokenExpiration);
    }

    public void logout(String refreshToken){
        tokenService.invalidateToken(refreshToken);
    }

    public AuthResponseDTO renewToken(String refreshToken){
        RefreshToken token = tokenService.findByToken(refreshToken);

        if (isTokenExpired(token)) {
            tokenService.invalidateToken(token);
            throw new InvalidTokenException("Refresh token expired. Please login again.");
        }

        // delete old token (rotation)
        tokenService.invalidateToken(token);
        String newToken = tokenService.createToken(token.getCostumer());
        String newRefreshToken = tokenService.createRefreshToken(token.getCostumer()).getToken();
        return new AuthResponseDTO(newToken, newRefreshToken, accessTokenExpiration);
    }

    private boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }


}
