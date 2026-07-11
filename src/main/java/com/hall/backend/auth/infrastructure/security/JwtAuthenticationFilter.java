package com.hall.backend.auth.infrastructure.security;

import com.hall.backend.auth.exception.AuthErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = resolveAccessToken(request);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            AuthenticatedMember authenticatedMember =
                    jwtTokenProvider.getAuthenticatedMember(accessToken);

            UsernamePasswordAuthenticationToken authentication =
                    createAuthentication(authenticatedMember);

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();

            authenticationEntryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException(
                            AuthErrorCode.INVALID_ACCESS_TOKEN.message(),
                            exception
                    )
            );
        }
    }

    private String resolveAccessToken(
            HttpServletRequest request
    ) {
        String authorization =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null
                || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String token = authorization
                .substring(BEARER_PREFIX.length())
                .trim();

        return token.isBlank() ? null : token;
    }

    private UsernamePasswordAuthenticationToken createAuthentication(
            AuthenticatedMember authenticatedMember
    ) {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        normalizeRole(authenticatedMember.role())
                );

        return UsernamePasswordAuthenticationToken.authenticated(
                authenticatedMember,
                null,
                List.of(authority)
        );
    }

    private String normalizeRole(String role) {
        return role.startsWith("ROLE_")
                ? role
                : "ROLE_" + role;
    }
}
