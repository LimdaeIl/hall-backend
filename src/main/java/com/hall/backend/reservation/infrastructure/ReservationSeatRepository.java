package com.hall.backend.reservation.infrastructure;

import com.hall.backend.reservation.domain.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

}
