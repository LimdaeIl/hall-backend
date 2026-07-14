package com.hall.backend.reservation.infrastructure;

import com.hall.backend.reservation.domain.Reservation;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByReservationNumber(String reservationNumber);

    Optional<Reservation> findByReservationNumber(String reservationNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select distinct r
        from Reservation r
        left join fetch r.reservationSeats rs
        left join fetch rs.performanceSeat ps
        where r.id = :reservationId
    """)
    Optional<Reservation> findByIdForPayment(@Param("reservationId") Long reservationId);
}