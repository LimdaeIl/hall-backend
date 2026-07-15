package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.ConcertStatus;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.presentation.dto.response.GetConcertPerformancesResponse;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.projection.ConcertPerformanceProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetConcertPerformancesService {

    private static final List<ConcertStatus> MEMBER_VISIBLE_CONCERT_STATUSES =
            List.of(
                    ConcertStatus.OPEN,
                    ConcertStatus.CLOSED
            );

    private static final List<PerformanceStatus> MEMBER_VISIBLE_PERFORMANCE_STATUSES =
            List.of(
                    PerformanceStatus.OPEN,
                    PerformanceStatus.SOLD_OUT,
                    PerformanceStatus.CLOSED,
                    PerformanceStatus.CANCELLED,
                    PerformanceStatus.COMPLETED
            );

    private final ConcertRepository concertRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public List<GetConcertPerformancesResponse> getPerformances(Long concertId) {
        validateConcertId(concertId);

        boolean concertExists = concertRepository
                .findByIdAndStatusIn(concertId, MEMBER_VISIBLE_CONCERT_STATUSES).isPresent();

        if (!concertExists) {
            throw new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND);
        }

        return performanceRepository.findConcertPerformances(
                        concertId,
                        MEMBER_VISIBLE_PERFORMANCE_STATUSES
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private GetConcertPerformancesResponse toResponse(
            ConcertPerformanceProjection projection
    ) {
        return new GetConcertPerformancesResponse(
                projection.getPerformanceId(),
                projection.getStartsAt(),
                projection.getReservationOpensAt(),
                projection.getReservationClosesAt(),
                projection.getMaxTicketsPerMember(),
                projection.getStatus(),
                projection.getAvailableSeatCount() == null
                        ? 0L
                        : projection.getAvailableSeatCount()
        );
    }

    private void validateConcertId(Long concertId) {
        if (concertId == null || concertId <= 0) {
            throw new ConcertException(
                    ConcertErrorCode.CONCERT_NOT_FOUND
            );
        }
    }
}
