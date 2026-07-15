package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;

public record GetConcertPerformancesResponse(
        Long performanceId,
        LocalDateTime startsAt,
        LocalDateTime reservationOpensAt,
        LocalDateTime reservationClosesAt,
        int maxTicketsPerMember,
        PerformanceStatus status,
        long availableSeatCount
) {
}
