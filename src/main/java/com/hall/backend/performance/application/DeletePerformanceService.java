package com.hall.backend.performance.application;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.response.DeletePerformanceResponse;
import com.hall.backend.reservation.infrastructure.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeletePerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository performanceSeatRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    @Transactional
    public DeletePerformanceResponse delete(Long performanceId) {
        validatePerformanceId(performanceId);

        Performance performance = performanceRepository.findByIdWithConcert(performanceId)
                .orElseThrow(() -> new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_FOUND));

        boolean hasReservationHistory = reservationSeatRepository.existsReservationHistoryByPerformanceId(performanceId);

        if (hasReservationHistory) {
            performance.cancelByAdmin();
            return DeletePerformanceResponse.cancelled(performance);
        }

        performanceSeatRepository.deleteAllByPerformanceId(performanceId);

        performanceRepository.deleteById(performanceId);

        return DeletePerformanceResponse.deleted(performanceId);
    }

    private void validatePerformanceId(Long performanceId) {
        if (performanceId == null || performanceId <= 0) {
            throw new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_FOUND);
        }
    }
}
