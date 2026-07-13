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
    RESERVATION_SEAT_REQUIRED(HttpStatus.BAD_REQUEST, "예약 좌석은 한 개 이상 필요합니다."), RESERVATION_NOT_FOUND(
            HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."
    );
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
