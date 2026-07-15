package com.hall.backend.performance.domain;

import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformanceSeatStatus {

    AVAILABLE("예약 가능"),
    HELD("임시 선점"),
    RESERVED("예약 완료"),
    BLOCKED("판매 중지");

    private final String description;

    public static PerformanceSeatStatus from(String status) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(status.trim()))
                .findFirst()
                .orElseThrow(() ->
                        new PerformanceException(
                                PerformanceErrorCode.INVALID_ADMIN_SEAT_STATUS
                        ));
    }
}

