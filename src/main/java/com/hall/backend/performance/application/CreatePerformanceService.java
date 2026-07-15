package com.hall.backend.performance.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.presentation.dto.request.CreatePerformanceRequest;
import com.hall.backend.performance.presentation.dto.response.CreatePerformanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreatePerformanceService {

    private final ConcertRepository concertRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional
    public CreatePerformanceResponse create(
            CreatePerformanceRequest request
    ) {
        Concert concert =
                concertRepository
                        .findById(request.concertId())
                        .orElseThrow(
                                () -> new PerformanceException(
                                        PerformanceErrorCode
                                                .CONCERT_NOT_FOUND
                                )
                        );

        validateDuplicatedPerformance(request);

        Performance performance =
                Performance.create(
                        concert,
                        request.startsAt(),
                        request.reservationOpensAt(),
                        request.reservationClosesAt(),
                        request.maxTicketsPerMember()
                );

        Performance savedPerformance =
                performanceRepository.save(
                        performance
                );

        return CreatePerformanceResponse.from(
                savedPerformance
        );
    }

    private void validateDuplicatedPerformance(
            CreatePerformanceRequest request
    ) {
        boolean exists =
                performanceRepository
                        .existsByConcertIdAndStartsAt(
                                request.concertId(),
                                request.startsAt()
                        );

        if (exists) {
            throw new PerformanceException(
                    PerformanceErrorCode
                            .PERFORMANCE_ALREADY_EXISTS
            );
        }
    }
}
