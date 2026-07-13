package com.hall.backend.performance.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PerformanceErrorCode implements ErrorCode {

    CONCERT_REQUIRED(HttpStatus.BAD_REQUEST, "콘서트는 필수입니다."),
    STARTS_AT_REQUIRED(HttpStatus.BAD_REQUEST, "공연 시작 시간은 필수입니다."),
    RESERVATION_OPENS_AT_REQUIRED(HttpStatus.BAD_REQUEST, "예약 시작 시간은 필수입니다."),
    RESERVATION_CLOSES_AT_REQUIRED(HttpStatus.BAD_REQUEST, "예약 종료 시간은 필수입니다."),
    INVALID_RESERVATION_PERIOD(HttpStatus.BAD_REQUEST, "유효하지 않은 예약 기간입니다."),
    RESERVATION_MUST_CLOSE_BEFORE_START(HttpStatus.BAD_REQUEST, "예약 종료 시간은 공연 시작 시간보다 이전이어야 합니다."),
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "콘서트를 찾을 수 없습니다."),
    PERFORMANCE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 공연입니다."),
    DUPLICATE_SEAT_GRADE_PRICE(HttpStatus.BAD_REQUEST, "중복된 좌석 등급 가격이 있습니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다."),
    SEAT_GRADE_PRICE_REQUIRED(HttpStatus.BAD_REQUEST, "좌석 등급별 가격은 필수입니다.");

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
