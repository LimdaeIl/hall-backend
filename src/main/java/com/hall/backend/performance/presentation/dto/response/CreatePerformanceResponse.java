package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import java.time.LocalDateTime;

public record CreatePerformanceResponse(
        Long performanceId,
        Long concertId,
        LocalDateTime startsAt,
        LocalDateTime reservationOpensAt,
        LocalDateTime reservationClosesAt,
        int maxTicketsPerMember,
        String status,
        int createdSeatCount
) {

    public static CreatePerformanceResponse of(
            Performance performance,
            int createdSeatCount
    ) {
        return new CreatePerformanceResponse(
                performance.getId(),
                performance.getConcert().getId(),
                performance.getStartsAt(),
                performance.getReservationOpensAt(),
                performance.getReservationClosesAt(),
                performance.getMaxTicketsPerMember(),
                performance.getStatus().name(),
                createdSeatCount
        );
    }
}
