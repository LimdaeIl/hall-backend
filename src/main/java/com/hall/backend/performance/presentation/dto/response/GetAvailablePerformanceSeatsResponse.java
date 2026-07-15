package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import java.time.LocalDateTime;
import java.util.List;

public record GetAvailablePerformanceSeatsResponse(
        Long performanceId,
        Long concertId,
        LocalDateTime startsAt,
        int maxTicketsPerMember,
        int availableSeatCount,
        List<GetAvailablePerformanceSeatResponse> seats
) {

    public static GetAvailablePerformanceSeatsResponse of(Performance performance,
            List<GetAvailablePerformanceSeatResponse> seats) {
        return new GetAvailablePerformanceSeatsResponse(
                performance.getId(),
                performance.getConcert().getId(),
                performance.getStartsAt(),
                performance.getMaxTicketsPerMember(),
                seats.size(),
                seats
        );
    }
}
