package com.jope.financetracker.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
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

    public void checkAccess(UUID ownerId) {
        if(isBasic() && !getCurrentUserId().equals(ownerId)) {
            throw new AccessDeniedException("Not authorized");
        }
    }

    public boolean isAdmin() {
        return this.getCurrentUserAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + Role.Values.ADMIN.name()));
    }

    public boolean isBasic() {
        return this.getCurrentUserAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + Role.Values.BASIC.name()));
    }

    public boolean hasRole(String role) {
        return this.getCurrentUserAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + role.toUpperCase()));
    }

    public boolean hasAnyRole(String... roles) {
        var authorities = this.getCurrentUserAuthorities();
        for (String role : roles) {
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + role.toUpperCase()))) {
                return true;
            }
        }
        return false;
    }

    private Collection<GrantedAuthority> getCurrentUserAuthorities(){
        return ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getAuthorities();
    }
}
