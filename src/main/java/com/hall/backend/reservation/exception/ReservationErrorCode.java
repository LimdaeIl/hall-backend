package com.hall.backend.reservation.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 예약은 완료할 수 없습니다."),
    RESERVATION_NOT_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 예약이 아닙니다."),
    RESERVATION_NOT_PENDING_PAYMENT(HttpStatus.BAD_REQUEST, "결제 대기 상태의 예약만 변경할 수 있습니다."),
    PERFORMANCE_SEAT_REQUIRED(HttpStatus.BAD_REQUEST, "공연 좌석은 필수입니다."),
    INVALID_PERFORMANCE_SEAT(HttpStatus.BAD_REQUEST, "유효하지 않은 공연 좌석입니다."),
    RESERVATION_NUMBER_REQUIRED(HttpStatus.BAD_REQUEST, "예약 번호는 필수입니다."),
    RESERVATION_NUMBER_TOO_LONG(HttpStatus.BAD_REQUEST, "예약 번호는 30자 이내로 입력해주세요."),
    MEMBER_REQUIRED(HttpStatus.BAD_REQUEST, "회원은 필수입니다."),
    PERFORMANCE_REQUIRED(HttpStatus.BAD_REQUEST, "공연은 필수입니다."),
    EXPIRED_AT_REQUIRED(HttpStatus.BAD_REQUEST, "만료 시간은 필수입니다."),
    RESERVATION_SEAT_REQUIRED(HttpStatus.BAD_REQUEST, "예약 좌석은 한 개 이상 필요합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_REQUIRED(HttpStatus.BAD_REQUEST, "예약은 필수입니다."),
    RESERVATION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),
    SEAT_REQUIRED(HttpStatus.BAD_REQUEST, "좌석은 필수입니다."),
    RESERVATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "예약 가능한 좌석 수를 초과했습니다."),
    INVALID_SEAT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 좌석 ID입니다."),
    DUPLICATE_SEAT(HttpStatus.BAD_REQUEST, "중복된 좌석이 있습니다."),
    SEAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "좌석을 찾을 수 없습니다."),
    SEAT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "사용할 수 없는 좌석입니다."),
    TICKET_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "예약 가능한 티켓 수를 초과했습니다."),
    INVALID_RESERVATION_SEAT(HttpStatus.BAD_REQUEST, "유효하지 않은 예약 좌석입니다."),
    COMPLETED_AT_REQUIRED(HttpStatus.BAD_REQUEST, "완료 시간은 필수입니다."),
    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 예약 상태입니다."),
    CANCELLED_AT_REQUIRED(HttpStatus.BAD_REQUEST, "취소 시간은 필수입니다."),
    CURRENT_TIME_REQUIRED(HttpStatus.BAD_REQUEST, "현재 시간은 필수입니다."),
    EXPIRES_AT_REQUIRED(HttpStatus.BAD_REQUEST, "만료 시간은 필수입니다."),
    TOTAL_AMOUNT_OVERFLOW(HttpStatus.BAD_REQUEST, "총 금액이 오버플로우되었습니다."),
    RESERVATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 예약을 조회할 권한이 없습니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "페이지 번호는 0 이상이어야 합니다."),
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "페이지 크기는 1 이상 100 이하여야 합니다."),
    RESERVATION_CANCELLATION_DEADLINE_PASSED(HttpStatus.CONFLICT, "공연이 시작된 이후에는 예약을 취소할 수 없습니다."),
    RESERVATION_NOT_CANCELLABLE(HttpStatus.CONFLICT, "현재 예약 상태에서는 예약을 취소할 수 없습니다."),
    INVALID_RESERVATION_DATE_RANGE(HttpStatus.BAD_REQUEST, "예약 조회 시작일은 종료일보다 이전이어야 합니다."),
    INVALID_SEARCH_ID(HttpStatus.BAD_REQUEST, "검색 조건 ID는 1 이상이어야 합니다.");


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
