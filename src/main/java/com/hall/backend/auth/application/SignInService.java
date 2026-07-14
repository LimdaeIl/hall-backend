package com.hall.backend.auth.application;

import com.hall.backend.auth.exception.AuthErrorCode;
import com.hall.backend.auth.exception.AuthException;
import com.hall.backend.auth.infrastructure.redis.TokenRepository;
import com.hall.backend.auth.infrastructure.security.JWTHashUtil;
import com.hall.backend.auth.infrastructure.security.JwtTokenProvider;
import com.hall.backend.auth.presentation.dto.request.SignInRequest;
import com.hall.backend.auth.presentation.dto.response.SignInResult;
import com.hall.backend.member.domain.Member;
import com.hall.backend.member.infrastructure.jpa.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignInService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final JWTHashUtil jwtHashUtil;

    @Transactional
    public SignInResult signIn(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_SIGN_IN));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_SIGN_IN);
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(),
                member.getRole().name()
        );

        String refreshToken =
                jwtTokenProvider.createRefreshToken(member.getId());

        long refreshTokenRemainingMillis =
                jwtTokenProvider.getRefreshTokenRemainingMillis(refreshToken);

        String hashedToken = jwtHashUtil.sha256(refreshToken);

        tokenRepository.save(
                member.getId(),
                hashedToken,
                Duration.ofMillis(refreshTokenRemainingMillis)
        );

        return SignInResult.of(
                member.getId(),
                accessToken,
                refreshToken,
                jwtTokenProvider.getRefreshTokenRemainingSeconds(refreshToken)
        );
    }
}
