package com.hall.backend.concert.domain;

import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SeatGrade {
    VIP("VIP등급"),
    R("R등급"),
    S("S등급"),
    A("A등급");

    private final String description;

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
