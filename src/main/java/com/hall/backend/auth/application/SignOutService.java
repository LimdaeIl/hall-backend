package com.hall.backend.auth.application;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import com.hall.backend.auth.infrastructure.redis.TokenRepository;
import com.hall.backend.auth.infrastructure.security.JWTHashUtil;
import com.hall.backend.auth.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignOutService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JWTHashUtil jwtHashUtil;

    @Transactional
    public void signOut(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN);
        }
        Long memberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken);
        String hashedRefreshToken = jwtHashUtil.sha256(refreshToken);

        String storedRefreshTokenHash = tokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN));

        if (!hashedRefreshToken.equals(storedRefreshTokenHash)) {
            throw new AuthException(AuthErrorCode.MISMATCH_REFRESH_TOKEN);
        }

        tokenRepository.deleteByMemberId(memberId);
    }
}

