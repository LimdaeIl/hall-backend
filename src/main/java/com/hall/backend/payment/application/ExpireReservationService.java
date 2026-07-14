package com.hall.backend.reservation.application;

import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExpireReservationService {

    private final ReservationRepository reservationRepository;
    private final Clock clock;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expire(Long reservationId) {
        Reservation reservation =
                reservationRepository.findByIdForPayment(reservationId)
                        .orElseThrow(() ->
                                new ReservationException(
                                        ReservationErrorCode
                                                .RESERVATION_NOT_FOUND
                                )
                        );

        LocalDateTime now = LocalDateTime.now(clock);

        if (reservation.isPendingPayment()
                && reservation.isExpired(now)) {
            reservation.expire(now);
        }
    }
}
