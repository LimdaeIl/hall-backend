package com.hall.backend.reservation.infrastructure;

import com.hall.backend.reservation.domain.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    boolean existsByReservationNumber(
            String reservationNumber
    );

    Optional<Reservation> findByReservationNumber(
            String reservationNumber
    );
}
