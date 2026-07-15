package com.hall.backend.performance.application;

import com.hall.backend.concert.domain.Seat;
import com.hall.backend.concert.domain.SeatGrade;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.SeatRepository;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceSeatRequest;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceSeatResponse;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdatePerformanceSeatService {

    private final PerformanceSeatRepository performanceSeatRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public UpdatePerformanceSeatResponse update(Long performanceSeatId,
            UpdatePerformanceSeatRequest request) {
        validatePerformanceSeatId(performanceSeatId);
        validateRequest(request);

        PerformanceSeat performanceSeat = performanceSeatRepository.findByIdForUpdate(
                        performanceSeatId)
                .orElseThrow(() -> new PerformanceException(PerformanceErrorCode.SEAT_NOT_FOUND));

        performanceSeat.validateAdministrativelyEditable();

        Seat seat = performanceSeat.getSeat();
        String normalizedSeatNumber = normalizeSeatNumber(request.seatNumber());
        validateDuplicatedSeatNumber(seat, normalizedSeatNumber);
        validateDuplicatedSeatPosition(seat, request.rowNumber(), request.columnNumber());
        seat.updateLayout(normalizedSeatNumber, request.rowNumber(), request.columnNumber());

        SeatGrade grade = resolveGrade(request.grade());

        performanceSeat.updateInformation(grade, request.price());

        PerformanceSeatStatus status = resolveStatus(request.status());

        performanceSeat.changeAdministrativeStatus(status);

        return UpdatePerformanceSeatResponse.from(performanceSeat);
    }

    private void validateRequest(UpdatePerformanceSeatRequest request) {
        if (request == null) {
            throw new PerformanceException(PerformanceErrorCode.NO_SEAT_UPDATE_FIELDS);
        }

        boolean hasUpdateField = hasText(request.seatNumber())
                || hasText(request.grade())
                || request.price() != null
                || request.rowNumber() != null
                || request.columnNumber() != null
                || hasText(request.status());

        if (!hasUpdateField) {
            throw new PerformanceException(PerformanceErrorCode.NO_SEAT_UPDATE_FIELDS);
        }
    }

    private void validateDuplicatedSeatNumber(Seat seat, String normalizedSeatNumber) {
        if (normalizedSeatNumber == null) {
            return;
        }

        if (normalizedSeatNumber.equals(
                seat.getSeatNumber()
        )) {
            return;
        }

        boolean duplicated = seatRepository.existsBySeatNumberAndIdNot(normalizedSeatNumber,
                seat.getId());

        if (duplicated) {
            throw new ConcertException(ConcertErrorCode.DUPLICATE_SEAT_NUMBER);
        }
    }

    private void validateDuplicatedSeatPosition(Seat seat, Integer requestedRowNumber,
            Integer requestedColumnNumber) {
        if (requestedRowNumber == null && requestedColumnNumber == null) {
            return;
        }

        int resolvedRowNumber = requestedRowNumber == null
                ? seat.getRowNumber()
                : requestedRowNumber;

        int resolvedColumnNumber = requestedColumnNumber == null
                ? seat.getColumnNumber()
                : requestedColumnNumber;

        boolean samePosition = resolvedRowNumber ==
                seat.getRowNumber()
                && resolvedColumnNumber ==
                seat.getColumnNumber();

        if (samePosition) {
            return;
        }

        boolean duplicated = seatRepository.existsByRowNumberAndColumnNumberAndIdNot(
                resolvedRowNumber,
                resolvedColumnNumber,
                seat.getId()
        );

        if (duplicated) {
            throw new ConcertException(ConcertErrorCode.DUPLICATE_SEAT_POSITION);
        }
    }

    private SeatGrade resolveGrade(String grade) {
        if (!hasText(grade)) {
            return null;
        }

        return SeatGrade.from(grade.trim());
    }

    private PerformanceSeatStatus resolveStatus(String status) {
        if (!hasText(status)) {
            return null;
        }

        PerformanceSeatStatus resolvedStatus = PerformanceSeatStatus.from(status);

        if (resolvedStatus != PerformanceSeatStatus.AVAILABLE
                && resolvedStatus != PerformanceSeatStatus.BLOCKED) {
            throw new PerformanceException(PerformanceErrorCode.INVALID_ADMIN_SEAT_STATUS);
        }

        return resolvedStatus;
    }

    private String normalizeSeatNumber(String seatNumber) {
        if (seatNumber == null) {
            return null;
        }

        String normalized = seatNumber.trim().toUpperCase(Locale.ROOT);

        if (normalized.isBlank()) {
            throw new ConcertException(ConcertErrorCode.SEAT_NUMBER_REQUIRED);
        }

        return normalized;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void validatePerformanceSeatId(Long performanceSeatId) {
        if (performanceSeatId == null || performanceSeatId <= 0) {
            throw new PerformanceException(PerformanceErrorCode.SEAT_NOT_FOUND);
        }
    }
}
