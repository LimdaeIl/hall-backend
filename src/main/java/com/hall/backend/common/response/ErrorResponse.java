package com.hall.backend.common.response;

import com.hall.backend.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String path,
        List<FieldError> errors
) {

    public static ErrorResponse of(
            ErrorCode errorCode,
            HttpServletRequest request
    ) {
        return of(
                errorCode,
                errorCode.message(),
                request
        );
    }

    public static ErrorResponse of(
            ErrorCode errorCode,
            String message,
            HttpServletRequest request
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.status().value(),
                message,
                request.getRequestURI(),
                List.of()
        );
    }

    public static ErrorResponse withErrors(
            ErrorCode errorCode,
            HttpServletRequest request,
            List<FieldError> errors
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.status().value(),
                errorCode.message(),
                request.getRequestURI(),
                errors
        );
    }

    public record FieldError(
            String field,
            String message
    ) {

        public static FieldError of(
                String field,
                String message
        ) {
            return new FieldError(field, message);
        }
    }
}
