package com.hall.backend.concert.domain;

import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import java.util.Arrays;

public enum SeatGrade {
    VIP,
    R,
    S,
    A;

    public static SeatGrade from(String value) {
        return Arrays.stream(values())
                .filter(grade -> grade.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new ConcertException(
                                ConcertErrorCode.INVALID_SEAT_GRADE
                        )
                );
    }
}
