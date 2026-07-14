package com.hall.backend.member.application;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import com.hall.backend.auth.infrastructure.redis.TokenRepository;
import com.hall.backend.auth.infrastructure.security.JWTHashUtil;
import com.hall.backend.auth.infrastructure.security.JwtTokenProvider;
import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.exception.MemberErrorCode;
import com.hall.backend.member.exception.MemberException;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteMeService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JWTHashUtil jwtHashUtil;

    @Transactional
    public void delete(MemberPrincipal principal, String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN);
        }

        Long authenticatedMemberId = principal.memberId();
        Long refreshTokenMemberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken);

        if (!authenticatedMemberId.equals(refreshTokenMemberId)) {
            throw new AuthException(AuthErrorCode.MISMATCH_REFRESH_TOKEN);
        }

        String storedRefreshTokenHash = tokenRepository.findByMemberId(authenticatedMemberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN));

        String requestedRefreshTokenHash = jwtHashUtil.sha256(refreshToken);

        if (!requestedRefreshTokenHash.equals(storedRefreshTokenHash)) {
            throw new AuthException(AuthErrorCode.MISMATCH_REFRESH_TOKEN);
        }
        Member member = memberRepository.findById(authenticatedMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.deactivate();

        tokenRepository.deleteByMemberId(authenticatedMemberId);
    }


}