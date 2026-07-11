package com.hall.backend.auth.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String issuer,
        String secret,
        Duration accessTokenExpiration,
        Duration refreshTokenExpiration
) {

    public JwtProperties {
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT issuer는 비어 있을 수 없습니다."
            );
        }

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT secret은 비어 있을 수 없습니다."
            );
        }

        if (accessTokenExpiration == null
                || accessTokenExpiration.isZero()
                || accessTokenExpiration.isNegative()) {
            throw new IllegalArgumentException(
                    "Access Token 만료 시간은 양수여야 합니다."
            );
        }

        if (refreshTokenExpiration == null
                || refreshTokenExpiration.isZero()
                || refreshTokenExpiration.isNegative()) {
            throw new IllegalArgumentException(
                    "Refresh Token 만료 시간은 양수여야 합니다."
            );
        }
    }
}
