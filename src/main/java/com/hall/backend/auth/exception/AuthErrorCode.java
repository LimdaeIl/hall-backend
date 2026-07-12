package com.hall.backend.auth.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "%s에 해당하는 회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "%s은 이미 사용 중인 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN, "탈퇴한 회원은 이용할 수 없습니다."),
    UNAUTHENTICATED_MEMBER(HttpStatus.UNAUTHORIZED, "현재 인증된 회원 정보가 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "저장된 Refresh Token을 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 요청을 수행할 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "유효한 인증 정보가 필요합니다."),
    INVALID_SIGN_IN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    TOKEN_STORE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "토큰 저장소에 접근할 수 없습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 유형입니다."),
    NOT_FOUND_BY_MEMBER_ID(HttpStatus.NOT_FOUND, "회원 ID에 해당하는 계정을 찾을 수 없습니다."),
    MISSING_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 누락되었습니다."),
    MISMATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}
