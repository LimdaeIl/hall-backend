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

@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_TYPE_CLAIM = "tokenType";

    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    private final JwtProperties properties;
    private final SecretKey secretKey;
    private final Clock clock;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.clock = Clock.systemUTC();
        this.secretKey = createSecretKey(properties.secret());
    }

    public String createAccessToken(Long memberId, String role) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.accessTokenExpiration());

        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(memberId.toString())
                .claim(ROLE_CLAIM, role)
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.refreshTokenExpiration());

        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(memberId.toString())
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public AuthenticatedMember getAuthenticatedMember(String accessToken) {
        Claims claims = parseClaims(accessToken);
        validateTokenType(claims, ACCESS_TOKEN_TYPE);

        Long memberId = parseMemberId(claims.getSubject());
        String role = claims.get(ROLE_CLAIM, String.class);

        if (role == null || role.isBlank()) {
            throw new JwtException("Access Token에 권한 정보가 없습니다.");
        }

        return new AuthenticatedMember(memberId, role);
    }

    public Long getMemberIdFromRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        validateTokenType(claims, REFRESH_TOKEN_TYPE);

        return parseMemberId(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(properties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void validateTokenType(Claims claims, String expectedType) {
        String actualType = claims.get(TOKEN_TYPE_CLAIM, String.class);

        if (!expectedType.equals(actualType)) {
            throw new JwtException("유효하지 않은 JWT 토큰 종류입니다.");
        }
    }

    private Long parseMemberId(String subject) {
        if (subject == null || subject.isBlank()) {
            throw new JwtException("JWT subject가 존재하지 않습니다.");
        }

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new JwtException(
                    "JWT subject 형식이 올바르지 않습니다.",
                    exception
            );
        }
    }

    private SecretKey createSecretKey(String base64Secret) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException(
                    "JWT secret은 충분한 길이의 Base64 인코딩 키여야 합니다.",
                    exception
            );
        }
    }
}
