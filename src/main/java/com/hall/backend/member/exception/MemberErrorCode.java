package com.hall.backend.member.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "이메일 필수값입니다."),
    PHONE_NUMBER_REQUIRED(HttpStatus.BAD_REQUEST, "휴대전화번호는 필수값입니다."),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호는 필수값입니다."),
    NAME_REQUIRED(HttpStatus.BAD_REQUEST, "이름은 필수값입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONE(HttpStatus.BAD_REQUEST, "이미 사용 중인 휴대전화번호입니다."),
    DUPLICATE_NAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 이름입니다.");


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
