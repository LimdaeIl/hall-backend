package com.hall.backend.performance.application;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceStatusRequest;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdatePerformanceStatusService {

    private final PerformanceRepository performanceRepository;

    @Transactional
    public UpdatePerformanceStatusResponse updateStatus(
            Long performanceId,
            UpdatePerformanceStatusRequest request
    ) {
        Performance performance = performanceRepository
                .findById(performanceId)
                .orElseThrow(() ->
                        new PerformanceException(
                                PerformanceErrorCode.PERFORMANCE_NOT_FOUND
                        )
                );

        changeStatus(
                performance,
                request.status()
        );

        return UpdatePerformanceStatusResponse.from(performance);
    }

    private void changeStatus(
            Performance performance,
            PerformanceStatus requestedStatus
    ) {
        switch (requestedStatus) {
            case OPEN -> performance.open();
            case CLOSED -> performance.close();
            case CANCELLED -> performance.cancel();
            case COMPLETED -> performance.complete();

            case PREPARING, SOLD_OUT ->
                    throw new PerformanceException(
                            PerformanceErrorCode
                                    .INVALID_PERFORMANCE_STATUS
                    );
        }
    }
}
