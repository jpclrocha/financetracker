package com.jope.financetracker.repository;

import com.jope.financetracker.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByCostumerId(UUID uuid);
    void deleteAllByCostumerId(UUID uuid);
}
