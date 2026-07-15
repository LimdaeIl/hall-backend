package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;

public record UpdatePerformanceSeatResponse(
        Long performanceSeatId,
        Long performanceId,
        Long seatId,
        String seatNumber,
        String grade,
        int rowNumber,
        int columnNumber,
        long price,
        PerformanceSeatStatus status,
        Long version
) {

    public static UpdatePerformanceSeatResponse from(
            PerformanceSeat performanceSeat
    ) {
        return new UpdatePerformanceSeatResponse(
                performanceSeat.getId(),
                performanceSeat.getPerformance().getId(),
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
