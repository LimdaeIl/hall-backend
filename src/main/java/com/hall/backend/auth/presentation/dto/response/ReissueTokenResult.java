package com.hall.backend.auth.presentation.dto.response;

public record ReissueTokenResult(
        Long memberId,
        String newAccessToken,
        String newRefreshToken,
        long refreshTokenRemainingSecond
) {

    public static ReissueTokenResult of(Long memberId, String newAccessToken,
            String newRefreshToken, long refreshTokenRemainingSecond) {
        return new ReissueTokenResult(memberId, newAccessToken, newRefreshToken,
                refreshTokenRemainingSecond);
    }
}

