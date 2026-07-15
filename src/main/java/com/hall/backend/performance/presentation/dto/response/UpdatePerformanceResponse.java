package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;

public record UpdatePerformanceResponse(
        Long performanceId,
        Long concertId,
        String concertTitle,
        LocalDateTime startsAt,
        LocalDateTime reservationOpensAt,
        LocalDateTime reservationClosesAt,
        int maxTicketsPerMember,
        PerformanceStatus status
) {

    public static UpdatePerformanceResponse from(Performance performance) {
        return new UpdatePerformanceResponse(
                performance.getId(),
                performance.getConcert().getId(),
                performance.getConcert().getTitle(),
                performance.getStartsAt(),
                performance.getReservationOpensAt(),
                performance.getReservationClosesAt(),
                performance.getMaxTicketsPerMember(),
                performance.getStatus()
        );
    }
}
