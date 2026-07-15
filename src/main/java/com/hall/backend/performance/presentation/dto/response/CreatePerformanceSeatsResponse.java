package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import java.util.List;

public record CreatePerformanceSeatsResponse(
        Long performanceId,
        Long concertId,
        int createdSeatCount,
        List<SeatResponse> seats
) {

    public static CreatePerformanceSeatsResponse of(
            Performance performance,
            List<PerformanceSeat> performanceSeats
    ) {
        List<SeatResponse> seatResponses =
                performanceSeats.stream()
                        .map(SeatResponse::from)
                        .toList();

        return new CreatePerformanceSeatsResponse(
                performance.getId(),
                performance.getConcert().getId(),
                seatResponses.size(),
                seatResponses
        );
    }

    public record SeatResponse(
            Long performanceSeatId,
            Long seatId,
            String seatNumber,
            String grade,
            int rowNumber,
            int columnNumber,
            long price,
            String status
    ) {

        public static SeatResponse from(
                PerformanceSeat performanceSeat
        ) {
            return new SeatResponse(
                    performanceSeat.getId(),
                    performanceSeat.getSeat().getId(),
                    performanceSeat.getSeat().getSeatNumber(),
                    performanceSeat.getGrade().name(),
                    performanceSeat.getSeat().getRowNumber(),
                    performanceSeat.getSeat().getColumnNumber(),
                    performanceSeat.getPrice(),
                    performanceSeat.getStatus().name()
            );
        }
    }
}
