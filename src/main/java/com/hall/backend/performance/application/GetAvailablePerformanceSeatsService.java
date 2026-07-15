package com.hall.backend.performance.application;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.response.GetAvailablePerformanceSeatResponse;
import com.hall.backend.performance.presentation.dto.response.GetAvailablePerformanceSeatsResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetAvailablePerformanceSeatsService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository performanceSeatRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public GetAvailablePerformanceSeatsResponse getAvailableSeats(Long performanceId) {
        Performance performance = performanceRepository
                .findById(performanceId)
                .orElseThrow(() -> new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now(clock);
        performance.validateReservable(now);

        List<PerformanceSeat> performanceSeats = performanceSeatRepository
                        .findAllByPerformanceIdAndStatus(performanceId, PerformanceSeatStatus.AVAILABLE);

        List<GetAvailablePerformanceSeatResponse> seats =
                performanceSeats.stream()
                        .map(GetAvailablePerformanceSeatResponse::from)
                        .toList();

        return GetAvailablePerformanceSeatsResponse.of(performance, seats);
    }
}
