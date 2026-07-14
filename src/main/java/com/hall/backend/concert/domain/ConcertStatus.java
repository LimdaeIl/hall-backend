package com.hall.backend.concert.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ConcertStatus {
    PREPARING("준비중"),
    OPEN("공연중"),
    CLOSED("종료"),
    CANCELLED("취소");

    private final String description;
}
