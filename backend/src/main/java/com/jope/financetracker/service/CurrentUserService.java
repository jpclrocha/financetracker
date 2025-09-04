package com.jope.financetracker.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return UUID.fromString(jwtAuth.getName());
        }

        throw new IllegalStateException("No authenticated user");
    }

    public void hasRole(){
        var authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getAuthorities());
    }
}
