package com.hall.backend.performance.application;

import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.response.DeletePerformanceSeatResponse;
import com.hall.backend.reservation.infrastructure.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeletePerformanceSeatService {

    private final PerformanceSeatRepository performanceSeatRepository;

    private final ReservationSeatRepository reservationSeatRepository;

    @Transactional
    public DeletePerformanceSeatResponse delete(Long performanceSeatId) {
        validatePerformanceSeatId(performanceSeatId);

        PerformanceSeat performanceSeat = performanceSeatRepository
                        .findByIdForUpdate(performanceSeatId)
                        .orElseThrow(() -> new PerformanceException(PerformanceErrorCode.SEAT_NOT_FOUND));

        validateNoReservationHistory(performanceSeatId);
        performanceSeat.deactivateByAdmin();
        return DeletePerformanceSeatResponse.from(performanceSeat);
    }

    private void validateNoReservationHistory(Long performanceSeatId) {
        boolean hasReservationHistory = reservationSeatRepository.existsByPerformanceSeat_Id(performanceSeatId);

        if (hasReservationHistory) {
            throw new PerformanceException(PerformanceErrorCode.SEAT_HAS_RESERVATION_HISTORY);
        }
    }

    private void validatePerformanceSeatId(Long performanceSeatId) {
        if (performanceSeatId == null || performanceSeatId <= 0) {
            throw new PerformanceException(PerformanceErrorCode.SEAT_NOT_FOUND);
        }
    }
}
