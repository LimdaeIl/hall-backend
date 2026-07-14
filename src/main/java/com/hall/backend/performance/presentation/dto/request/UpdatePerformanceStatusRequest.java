package com.hall.backend.performance.presentation.dto.request;

import com.hall.backend.performance.domain.PerformanceStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePerformanceStatusRequest(

        @NotNull(message = "공연 상태는 필수입니다.")
        PerformanceStatus status
) {
}
