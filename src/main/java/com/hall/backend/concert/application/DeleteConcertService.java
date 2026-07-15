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
    private final PerformanceSeatRepository performanceSeatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void delete(Long concertId) {
        if (concertId == null || concertId <= 0) {
            throw new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND);
        }

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND));

        boolean hasReservations = reservationRepository.existsByPerformance_Concert_Id(concertId);

        if (hasReservations) {
            throw new ConcertException(ConcertErrorCode.CONCERT_HAS_RESERVATIONS);
        }

        performanceSeatRepository.deleteAllByConcertId(concertId);
        performanceRepository.deleteAllByConcertId(concertId);
        concertRepository.delete(concert);
    }
}
