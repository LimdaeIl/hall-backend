package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;
import com.hall.backend.performance.domain.PerformanceStatus;
import java.time.LocalDateTime;
import java.util.List;

public record GetAdminPerformanceSeatsResponse(
        Long performanceId,
        Long concertId,
        String concertTitle,
        LocalDateTime startsAt,
        PerformanceStatus performanceStatus,
        int totalSeatCount,
        long availableSeatCount,
        long heldSeatCount,
        long reservedSeatCount,
        long blockedSeatCount,
        List<SeatResponse> seats
) {

    public static GetAdminPerformanceSeatsResponse of(Performance performance, List<PerformanceSeat> performanceSeats) {
        List<SeatResponse> seatResponses =
                performanceSeats.stream()
                        .map(SeatResponse::from)
                        .toList();

        long availableSeatCount = countByStatus(performanceSeats, PerformanceSeatStatus.AVAILABLE);

        long heldSeatCount = countByStatus(performanceSeats, PerformanceSeatStatus.HELD);

        long reservedSeatCount = countByStatus(performanceSeats, PerformanceSeatStatus.RESERVED);

        long blockedSeatCount = countByStatus(performanceSeats, PerformanceSeatStatus.BLOCKED);

        return new GetAdminPerformanceSeatsResponse(
                performance.getId(),
                performance.getConcert().getId(),
                performance.getConcert().getTitle(),
                performance.getStartsAt(),
                performance.getStatus(),
                performanceSeats.size(),
                availableSeatCount,
                heldSeatCount,
                reservedSeatCount,
                blockedSeatCount,
                seatResponses
        );
    }

    private static long countByStatus(List<PerformanceSeat> performanceSeats, PerformanceSeatStatus status) {
        return performanceSeats.stream()
                .filter(performanceSeat -> performanceSeat.getStatus() == status)
                .count();
    }

    public record SeatResponse(
            Long performanceSeatId,
            Long seatId,
            String seatNumber,
            String grade,
            int rowNumber,
            int columnNumber,
            long price,
            PerformanceSeatStatus status,
            Long version
    ) {

        public static SeatResponse from(PerformanceSeat performanceSeat) {
            return new SeatResponse(
                    performanceSeat.getId(),
                    performanceSeat.getSeat().getId(),
                    performanceSeat.getSeat().getSeatNumber(),
                    performanceSeat.getGrade().name(),
                    performanceSeat.getSeat().getRowNumber(),
                    performanceSeat.getSeat().getColumnNumber(),
                    performanceSeat.getPrice(),
                    performanceSeat.getStatus(),
                    performanceSeat.getVersion()
            );
        }
    }
}
