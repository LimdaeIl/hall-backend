package com.hall.backend.auth.application;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import com.hall.backend.auth.infrastructure.redis.TokenRepository;
import com.hall.backend.auth.infrastructure.security.JWTHashUtil;
import com.hall.backend.auth.infrastructure.security.JwtTokenProvider;
import com.hall.backend.auth.presentation.dto.response.ReissueTokenResult;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReissueTokenService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JWTHashUtil jwtHashUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ReissueTokenResult reissue(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN);
        }

        Long memberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUND_BY_MEMBER_ID));

        String newAccessToken = jwtTokenProvider.createAccessToken(
                member.getId(),
                member.getRole().name()
        );
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        String hashedOldRefreshToken = jwtHashUtil.sha256(refreshToken);
        String hashedNewRefreshToken = jwtHashUtil.sha256(newRefreshToken);

        Duration refreshTokenTtl =
                Duration.ofMillis(jwtTokenProvider.getRefreshTokenExpirationMillis());

        tokenRepository.rotateIfMatches(
                member.getId(),
                hashedOldRefreshToken,
                hashedNewRefreshToken,
                refreshTokenTtl
        );

        return ReissueTokenResult.of(
                member.getId(),
                newAccessToken,
                newRefreshToken,
                refreshTokenTtl.toSeconds()
        );
    }
}

