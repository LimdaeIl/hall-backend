package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;

public record GetAdminPerformancesResponse(
        Long performanceId,
        Long concertId,
        String concertTitle,
        String artist,
        PerformanceStatus status,
        LocalDateTime startsAt,
        LocalDateTime reservationOpensAt,
        LocalDateTime reservationClosesAt,
        int maxTicketsPerMember
) {

    public static GetAdminPerformancesResponse from(
            Performance performance
    ) {
        return new GetAdminPerformancesResponse(
                performance.getId(),
                performance.getConcert().getId(),
                performance.getConcert().getTitle(),
                performance.getConcert().getArtist(),
                performance.getStatus(),
                performance.getStartsAt(),
                performance.getReservationOpensAt(),
                performance.getReservationClosesAt(),
                performance.getMaxTicketsPerMember()
        );
    }
}
