package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.performance.infrastructure.PerformanceRepository;
import com.hall.backend.performance.infrastructure.PerformanceSeatRepository;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteConcertService {

    private final ConcertRepository concertRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository
            performanceSeatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void delete(Long concertId) {
        validateConcertId(concertId);

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(
                        () -> new ConcertException(
                                ConcertErrorCode.CONCERT_NOT_FOUND
                        )
                );

        validateNoReservations(concertId);

        performanceSeatRepository.deleteAllByConcertId(
                concertId
        );

        performanceRepository.deleteAllByConcertId(
                concertId
        );

        concertRepository.delete(concert);
    }

    private void validateNoReservations(
            Long concertId
    ) {
        boolean hasReservations =
                reservationRepository
                        .existsByPerformance_Concert_Id(
                                concertId
                        );

        if (hasReservations) {
            throw new ConcertException(
                    ConcertErrorCode.CONCERT_HAS_RESERVATIONS
            );
        }
    }

    private void validateConcertId(Long concertId) {
        if (concertId == null || concertId <= 0) {
            throw new ConcertException(
                    ConcertErrorCode.CONCERT_NOT_FOUND
            );
        }
    }
}
