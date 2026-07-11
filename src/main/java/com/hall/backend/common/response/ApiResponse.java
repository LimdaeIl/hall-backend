package com.hall.backend.common.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        LocalDateTime timestamp,
        int status,
        String message,
        T data
) {

    public static <T> ApiResponse<T> ok(T data) {
        return of(
                200,
                "요청이 성공적으로 처리되었습니다.",
                data
        );
    }

    public static <T> ApiResponse<T> ok(
            String message,
            T data
    ) {
        return of(200, message, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return of(
                201,
                "리소스가 성공적으로 생성되었습니다.",
                data
        );
    }

    public static <T> ApiResponse<T> created(
            String message,
            T data
    ) {
        return of(201, message, data);
    }

    private static <T> ApiResponse<T> of(
            int status,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                status,
                message,
                data
        );
    }
}
