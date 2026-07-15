package com.hall.backend.performance.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformanceStatus {

    PREPARING("공연 준비"),
    OPEN("예매 중"),
    SOLD_OUT("매진"),
    CLOSED("예매 종료"),
    CANCELLED("공연 취소"),
    COMPLETED("공연 종료");

    private final String description;
}

