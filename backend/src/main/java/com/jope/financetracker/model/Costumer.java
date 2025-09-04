package com.jope.financetracker.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.jope.financetracker.config.uuid_v7.GeneratedUuidV7;
import com.jope.financetracker.dto.login.LoginRequestDTO;
import com.jope.financetracker.enums.Currency;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Costumer {
    @Id
    @GeneratedUuidV7
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Currency currency;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "costumer_roles", 
        joinColumns = @JoinColumn(name = "costumer_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    private Set<Role> roles;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "costumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "costumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecurringTransaction> recurringTransactions = new HashSet<>();

    @OneToMany(mappedBy = "costumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "costumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Budget> budgets = new HashSet<>();

    public boolean isLoginCorrect(LoginRequestDTO loginRequest, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(loginRequest.password(), this.password);
    }
}
