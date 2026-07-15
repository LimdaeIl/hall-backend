package com.hall.backend.reservation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    PENDING_PAYMENT("결제 대기"),
    COMPLETED("예약 완료"),
    CANCELLED("예약 취소"),
    EXPIRED("예약 만료");

    private final String description;
}

