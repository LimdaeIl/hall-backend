package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;

public record UpdatePerformanceStatusResponse(
        Long performanceId,
        Long concertId,
        PerformanceStatus status,
        LocalDateTime startsAt,
        LocalDateTime reservationOpensAt,
        LocalDateTime reservationClosesAt
) {

    public static UpdatePerformanceStatusResponse from(Performance performance) {
        return new UpdatePerformanceStatusResponse(
                performance.getId(),
                performance.getConcert().getId(),
                performance.getStatus(),
                performance.getStartsAt(),
                performance.getReservationOpensAt(),
                performance.getReservationClosesAt()
        );
    }
}
