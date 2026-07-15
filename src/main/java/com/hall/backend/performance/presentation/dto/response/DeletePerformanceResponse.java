package com.hall.backend.performance.presentation.dto.response;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;

public record DeletePerformanceResponse(
        Long performanceId,
        DeleteType deleteType,
        PerformanceStatus status
) {

    public static DeletePerformanceResponse deleted(
            Long performanceId
    ) {
        return new DeletePerformanceResponse(
                performanceId,
                DeleteType.DELETED,
                null
        );
    }

    public static DeletePerformanceResponse cancelled(
            Performance performance
    ) {
        return new DeletePerformanceResponse(
                performance.getId(),
                DeleteType.CANCELLED,
                performance.getStatus()
        );
    }

    public enum DeleteType {
        DELETED,
        CANCELLED
    }
}
