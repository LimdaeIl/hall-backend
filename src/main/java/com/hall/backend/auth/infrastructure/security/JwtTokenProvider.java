package com.hall.backend.auth.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_TYPE_CLAIM = "tokenType";

    private final JwtProperties properties;
    private final SecretKey secretKey;
    private final Clock clock;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = createSecretKey(properties.base64Secret());
        this.clock = Clock.systemUTC();
    }

    public String createAccessToken(
            Long memberId,
            String role
    ) {
        validateMemberId(memberId);

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "회원 권한은 비어 있을 수 없습니다."
            );
        }

        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(
                properties.accessTokenExpiration()
        );

        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(memberId.toString())
                .claim(ROLE_CLAIM, role)
                .claim(
                        TOKEN_TYPE_CLAIM,
                        TokenType.ACCESS.name()
                )
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        validateMemberId(memberId);

        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(
                properties.refreshTokenExpiration()
        );

        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(memberId.toString())
                .claim(
                        TOKEN_TYPE_CLAIM,
                        TokenType.REFRESH.name()
                )
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public AuthenticatedMember getAuthenticatedMember(
            String accessToken
    ) {
        Claims claims = parseClaims(accessToken);

        validateTokenType(
                claims,
                TokenType.ACCESS
        );

        Long memberId = parseMemberId(claims);
        String role = claims.get(
                ROLE_CLAIM,
                String.class
        );

        if (role == null || role.isBlank()) {
            throw new JwtException(
                    "Access Token에 권한 정보가 없습니다."
            );
        }

        return new AuthenticatedMember(
                memberId,
                role
        );
    }

    public Long getMemberIdFromRefreshToken(
            String refreshToken
    ) {
        Claims claims = parseClaims(refreshToken);

        validateTokenType(
                claims,
                TokenType.REFRESH
        );

        return parseMemberId(claims);
    }

    private Claims parseClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new JwtException(
                    "JWT가 비어 있습니다."
            );
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(properties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void validateTokenType(
            Claims claims,
            TokenType expectedType
    ) {
        String actualType = claims.get(
                TOKEN_TYPE_CLAIM,
                String.class
        );

        if (!expectedType.name().equals(actualType)) {
            throw new JwtException(
                    "JWT 종류가 올바르지 않습니다."
            );
        }
    }

    private Long parseMemberId(Claims claims) {
        String subject = claims.getSubject();

        if (subject == null || subject.isBlank()) {
            throw new JwtException(
                    "JWT에 회원 식별자가 없습니다."
            );
        }

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new JwtException(
                    "JWT 회원 식별자 형식이 올바르지 않습니다.",
                    exception
            );
        }
    }

    private void validateMemberId(Long memberId) {
        Objects.requireNonNull(
                memberId,
                "회원 ID는 null일 수 없습니다."
        );

        if (memberId <= 0) {
            throw new IllegalArgumentException(
                    "회원 ID는 양수여야 합니다."
            );
        }
    }

    private SecretKey createSecretKey(
            String base64Secret
    ) {
        try {
            byte[] keyBytes =
                    Decoders.BASE64.decode(base64Secret);

            return Keys.hmacShaKeyFor(keyBytes);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException(
                    "JWT 비밀 키는 충분한 길이의 Base64 문자열이어야 합니다.",
                    exception
            );
        }
    }

    private enum TokenType {
        ACCESS,
        REFRESH
    }
}
