package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.concert.domain.Seat;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;

public record GetAvailablePerformanceSeatResponse(
        Long performanceSeatId,
        Long seatId,
        String seatNumber,
        String grade,
        int rowNumber,
        int columnNumber,
        long price,
        PerformanceSeatStatus status
) {

    public static GetAvailablePerformanceSeatResponse from(PerformanceSeat performanceSeat) {
        Seat seat = performanceSeat.getSeat();

        return new GetAvailablePerformanceSeatResponse(
                performanceSeat.getId(),
                seat.getId(),
                seat.getSeatNumber(),
                performanceSeat.getGrade().name(),
                seat.getRowNumber(),
                seat.getColumnNumber(),
                performanceSeat.getPrice(),
                performanceSeat.getStatus()
        );
    }
}
