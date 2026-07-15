package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;

public record DeletePerformanceSeatResponse(
        Long performanceSeatId,
        Long performanceId,
        Long seatId,
        String seatNumber,
        PerformanceSeatStatus status
) {

    public static DeletePerformanceSeatResponse from(PerformanceSeat performanceSeat) {
        return new DeletePerformanceSeatResponse(
                performanceSeat.getId(),
                performanceSeat.getPerformance().getId(),
                performanceSeat.getSeat().getId(),
                performanceSeat.getSeat().getSeatNumber(),
                performanceSeat.getStatus()
        );
    }
}
