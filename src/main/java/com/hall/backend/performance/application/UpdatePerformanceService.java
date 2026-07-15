package com.hall.backend.performance.application;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.presentation.dto.request.UpdatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.response.UpdatePerformanceResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdatePerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional
    public UpdatePerformanceResponse update(Long performanceId, UpdatePerformanceRequest request) {
        validatePerformanceId(performanceId);
        validateRequest(request);

        Performance performance = performanceRepository.findByIdWithConcert(performanceId)
                .orElseThrow(() -> new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_FOUND));

        LocalDateTime updatedStartsAt = request.startsAt() == null
                        ? performance.getStartsAt()
                        : request.startsAt();

        validateDuplicatedStartsAt(performance, updatedStartsAt);

        performance.updateInformation(
                request.startsAt(),
                request.reservationOpensAt(),
                request.reservationClosesAt(),
                request.maxTicketsPerMember()
        );

        return UpdatePerformanceResponse.from(performance);
    }

    private void validateDuplicatedStartsAt(Performance performance, LocalDateTime startsAt) {
        if (startsAt.equals(performance.getStartsAt())) {
            return;
        }

        boolean duplicated = performanceRepository.existsByConcertIdAndStartsAtAndIdNot(
                                performance.getConcert().getId(),
                                startsAt,
                                performance.getId()
                        );

        if (duplicated) {
            throw new PerformanceException(PerformanceErrorCode.PERFORMANCE_ALREADY_EXISTS);
        }
    }

    private void validateRequest(UpdatePerformanceRequest request) {
        if (request == null) {
            throw new PerformanceException(PerformanceErrorCode.NO_PERFORMANCE_UPDATE_FIELDS);
        }

        boolean hasUpdateField = request.startsAt() != null
                        || request.reservationOpensAt() != null
                        || request.reservationClosesAt() != null
                        || request.maxTicketsPerMember() != null;

        if (!hasUpdateField) {
            throw new PerformanceException(PerformanceErrorCode.NO_PERFORMANCE_UPDATE_FIELDS);
        }
    }

    private void validatePerformanceId(Long performanceId) {
        if (performanceId == null || performanceId <= 0) {
            throw new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_FOUND);
        }
    }
}
