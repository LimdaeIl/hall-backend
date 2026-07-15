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
    CURRENT_TIME_REQUIRED(HttpStatus.BAD_REQUEST, "현재 시간은 필수입니다."),
    INVALID_RESERVATION_PERIOD(HttpStatus.BAD_REQUEST, "유효하지 않은 예약 기간입니다."),
    RESERVATION_MUST_CLOSE_BEFORE_START(HttpStatus.BAD_REQUEST, "예약 종료 시간은 공연 시작 시간보다 이전이어야 합니다."),
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "콘서트를 찾을 수 없습니다."),
    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "공연을 찾을 수 없습니다."),
    PERFORMANCE_REQUIRED(HttpStatus.BAD_REQUEST, "공연 회차는 필수입니다."),
    PERFORMANCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 공연입니다."),
    DUPLICATE_SEAT_GRADE_PRICE(HttpStatus.BAD_REQUEST, "중복된 좌석 등급 가격이 있습니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다."),
    SEAT_REQUIRED(HttpStatus.BAD_REQUEST, "좌석은 필수입니다."),
    SEAT_GRADE_REQUIRED(HttpStatus.BAD_REQUEST, "공연 좌석 등급은 필수입니다."),
    SEAT_GRADE_PRICE_REQUIRED(HttpStatus.BAD_REQUEST, "좌석 등급별 가격은 필수입니다."),
    INVALID_SEAT_PRICE(HttpStatus.BAD_REQUEST, "공연 좌석 가격은 0원 이상이어야 합니다."),
    SEAT_NOT_AVAILABLE(HttpStatus.CONFLICT, "예약 가능한 좌석이 아닙니다."),
    SEAT_NOT_HELD(HttpStatus.CONFLICT, "선점된 좌석이 아닙니다."),
    SEAT_NOT_BLOCKED(HttpStatus.CONFLICT, "차단된 좌석이 아닙니다."),
    PERFORMANCE_NOT_RESERVABLE(HttpStatus.CONFLICT, "현재 예약할 수 없는 공연입니다."),
    INVALID_PERFORMANCE_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 공연 상태입니다."),
    INVALID_MAX_TICKETS_PER_MEMBER(HttpStatus.BAD_REQUEST, "유효하지 않은 최대 티켓 수입니다."),
    RESERVATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "예약 가능한 티켓 수를 초과했습니다."),
    INVALID_TICKET_COUNT(HttpStatus.BAD_REQUEST, "유효하지 않은 티켓 수입니다."),
    NO_SEAT_UPDATE_FIELDS(HttpStatus.BAD_REQUEST, "수정할 좌석 정보가 없습니다."),
    INVALID_ADMIN_SEAT_STATUS(HttpStatus.BAD_REQUEST, "관리자가 지정할 수 있는 좌석 상태는 AVAILABLE 또는 BLOCKED입니다."),
    HELD_SEAT_CANNOT_BE_UPDATED(HttpStatus.CONFLICT, "임시 선점된 좌석은 수정할 수 없습니다."),
    RESERVED_SEAT_CANNOT_BE_UPDATED(HttpStatus.CONFLICT, "예약 완료된 좌석은 수정할 수 없습니다."),
    SEAT_ALREADY_BLOCKED(HttpStatus.CONFLICT, "이미 사용 불가 처리된 좌석입니다."),
    HELD_SEAT_CANNOT_BE_BLOCKED(HttpStatus.CONFLICT, "임시 선점 중인 좌석은 사용 불가 처리할 수 없습니다."),
    RESERVED_SEAT_CANNOT_BE_BLOCKED(HttpStatus.CONFLICT, "예약 완료된 좌석은 사용 불가 처리할 수 없습니다."),
    SEAT_HAS_RESERVATION_HISTORY(HttpStatus.CONFLICT, "예약 이력이 존재하는 좌석은 사용 불가 처리할 수 없습니다."),
    PERFORMANCE_SEATS_ALREADY_EXIST(HttpStatus.CONFLICT, "해당 공연의 좌석이 이미 생성되어 있습니다."),
    NO_AVAILABLE_BASE_SEATS(HttpStatus.NOT_FOUND, "공연에 연결할 공연장 좌석이 존재하지 않습니다."),
    PERFORMANCE_SEATS_REQUIRED(HttpStatus.CONFLICT, "공연을 오픈하기 전에 공연 좌석을 생성해야 합니다."),
    INVALID_SEAT_CANCELLATION_STATUS(HttpStatus.CONFLICT, "유효하지 않은 좌석 취소 상태입니다."),
    NO_PERFORMANCE_UPDATE_FIELDS(HttpStatus.BAD_REQUEST, "수정할 공연 정보가 없습니다."),;

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
