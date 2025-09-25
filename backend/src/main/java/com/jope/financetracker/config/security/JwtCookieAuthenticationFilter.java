package com.jope.financetracker.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        if (accessTokenCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = accessTokenCookie.getValue();
        try {
            Jwt jwt = jwtDecoder.decode(tokenValue);

            JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                    jwt,
                    jwt.getClaimAsStringList("scope").stream()
                            .map(scope -> (org.springframework.security.core.GrantedAuthority) () -> "SCOPE_" + scope)
                            .collect(Collectors.toList())
            );

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            logger.debug("Invalid JWT received from cookie: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
