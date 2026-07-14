package com.hall.backend.payment.exception;

import com.hall.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
    RESERVATION_REQUIRED(HttpStatus.BAD_REQUEST, "예약 정보는 필수입니다."),
    PAYMENT_METHOD_REQUIRED(HttpStatus.BAD_REQUEST, "결제 수단은 필수입니다."),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액은 0보다 커야 합니다."),
    TRANSACTION_KEY_REQUIRED(HttpStatus.BAD_REQUEST, "결제 거래 키는 필수입니다."),
    PAID_AT_REQUIRED(HttpStatus.BAD_REQUEST, "결제 완료 시간은 필수입니다."),
    CANCELLED_AT_REQUIRED(HttpStatus.BAD_REQUEST, "결제 취소 시간은 필수입니다."),
    PAYMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 예약을 결제할 권한이 없습니다."),
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 결제가 생성된 예약입니다."),
    INVALID_PAYMENT_STATUS(HttpStatus.CONFLICT, "현재 결제 상태에서는 요청을 처리할 수 없습니다."),
    PAYMENT_NOT_COMPLETED(HttpStatus.CONFLICT, "완료된 결제만 취소할 수 있습니다."),
    UNSUPPORTED_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "현재 지원하지 않는 결제 수단입니다.");

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
