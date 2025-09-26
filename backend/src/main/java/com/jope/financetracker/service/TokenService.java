package com.jope.financetracker.service;

import com.fasterxml.uuid.Generators;
import com.jope.financetracker.exceptions.InvalidTokenException;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.RefreshToken;
import com.jope.financetracker.model.Role;
import com.jope.financetracker.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtEncoder jwtEncoder;

    public TokenService(RefreshTokenRepository repo, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = repo;
        this.jwtEncoder = jwtEncoder;
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new InvalidTokenException("Invalid refresh token."));
    }

    public void invalidateToken(String token){
        refreshTokenRepository
                .findByToken(token)
                .ifPresentOrElse(
                    refreshTokenRepository::delete,
                    () -> {throw new InvalidTokenException("Invalid refresh token.");}
        );
    }

    public void invalidateToken(RefreshToken token){
        refreshTokenRepository.delete(token);
    }

    public String createToken(Customer customer){
        var scopes = customer.getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

        Instant now = Instant.now();

        var claims = JwtClaimsSet
                .builder()
                .issuer("financetracker_backend")
                .subject(customer.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusMillis(accessTokenExpiration))
                .claim("scope", scopes)
                .claim("email", customer.getEmail())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public RefreshToken createRefreshToken(Customer customer) {
        String token = Generators.timeBasedEpochGenerator().generate().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCustomer(customer);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setToken(token);
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllRefreshTokensForUser(UUID userId){
        refreshTokenRepository.deleteAllByCustomerId(userId);
    }
}
