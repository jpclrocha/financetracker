package com.jope.financetracker.service;

import com.jope.financetracker.dto.auth.AuthRequestDTO;
import com.jope.financetracker.dto.auth.AuthResponseDTO;
import com.jope.financetracker.exceptions.InvalidTokenException;
import com.jope.financetracker.model.Customer;
import com.jope.financetracker.model.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {


    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(TokenService tokenService, CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO login(AuthRequestDTO obj){
        Optional<Customer> opt = customerService.findByEmail(obj.email());

        if(opt.isEmpty() || !opt.get().isLoginCorrect(obj, passwordEncoder)){
            throw new BadCredentialsException("Username or password is invalid!");
        }

        Customer cos = opt.get();
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
        String newToken = tokenService.createToken(token.getCustomer());
        String newRefreshToken = tokenService.createRefreshToken(token.getCustomer()).getToken();
        return new AuthResponseDTO(newToken, newRefreshToken, accessTokenExpiration);
    }

    private boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }


}
