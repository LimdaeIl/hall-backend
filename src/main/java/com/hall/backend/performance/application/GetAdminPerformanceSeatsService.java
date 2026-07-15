package com.hall.backend.performance.application;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.response.GetAdminPerformanceSeatsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAdminPerformanceSeatsService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository
            performanceSeatRepository;

    @Transactional(readOnly = true)
    public GetAdminPerformanceSeatsResponse getSeats(
            Long performanceId
    ) {
        validatePerformanceId(performanceId);

        Performance performance =
                performanceRepository.findById(performanceId)
                        .orElseThrow(
                                () -> new PerformanceException(
                                        PerformanceErrorCode
                                                .PERFORMANCE_NOT_FOUND
                                )
                        );

        List<PerformanceSeat> performanceSeats =
                performanceSeatRepository
                        .findAllByPerformanceId(
                                performanceId
                        );

        return GetAdminPerformanceSeatsResponse.of(
                performance,
                performanceSeats
        );
    }

    private void validatePerformanceId(
            Long performanceId
    ) {
        if (performanceId == null || performanceId <= 0) {
            throw new PerformanceException(
                    PerformanceErrorCode.PERFORMANCE_NOT_FOUND
            );
        }
    }
}
