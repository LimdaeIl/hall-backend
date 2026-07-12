package com.hall.backend.auth.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String issuer,
        String base64Secret,
        Duration accessTokenExpiration,
        Duration refreshTokenExpiration
) {

    public JwtProperties {
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException("JWT 발급자 정보는 비어 있을 수 없습니다.");
        }

        if (base64Secret == null || base64Secret.isBlank()) {
            throw new IllegalArgumentException("JWT 비밀 키는 비어 있을 수 없습니다.");
        }

        validateExpiration(accessTokenExpiration, "Access Token");
        validateExpiration(refreshTokenExpiration, "Refresh Token");
    }

    private static void validateExpiration(
            Duration expiration,
            String tokenName
    ) {
        if (expiration == null
                || expiration.isZero()
                || expiration.isNegative()) {
            throw new IllegalArgumentException(tokenName + " 만료 시간은 양수여야 합니다.");
        }
    }
}