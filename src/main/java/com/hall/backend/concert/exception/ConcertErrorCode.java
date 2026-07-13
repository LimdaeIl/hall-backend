package com.hall.backend.concert.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
public enum ConcertErrorCode implements ErrorCode {
    TITLE_CANNOT_BE_NULL_OR_BLANK(HttpStatus.BAD_REQUEST, "제목은 null 또는 공백일 수 없습니다."),
    ARTIST_CANNOT_BE_NULL_OR_BLANK(HttpStatus.BAD_REQUEST, "아티스트는 null 또는 공백일 수 없습니다."),
    TITLE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 제목입니다.");

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
