package com.jope.financetracker.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.jope.financetracker.model.Role;

@Service
public class CurrentUserService {

    private static final String ROLE_PREFIX = "SCOPE_";

    public UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return UUID.fromString(jwtAuth.getName());
        }

        throw new IllegalStateException("No authenticated user");
    }

    public boolean hasRole(String role) {
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + role.toUpperCase()));
    }

    public boolean isAdmin() {
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + Role.Values.ADMIN.name()));
    }

    public boolean isBasic() {
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + Role.Values.BASIC.name()));
    }
}
