package com.hall.backend.auth.infrastructure.security;

import java.time.LocalDateTime;

public record SecurityErrorResponse(
        LocalDateTime timestamp,
        int status,
        String code,
        String message,
        String path
) {

    public static SecurityErrorResponse unauthorized(String path) {
        return new SecurityErrorResponse(
                LocalDateTime.now(),
                401,
                "AUTH_001",
                "인증이 필요합니다.",
                path
        );
    }

    public static SecurityErrorResponse forbidden(String path) {
        return new SecurityErrorResponse(
                LocalDateTime.now(),
                403,
                "AUTH_002",
                "접근 권한이 없습니다.",
                path
        );
    }
}
