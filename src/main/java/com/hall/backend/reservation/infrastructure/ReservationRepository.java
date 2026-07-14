package com.hall.backend.reservation.infrastructure;

import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationStatus;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select distinct r
            from Reservation r
            join fetch r.member
            join fetch r.performance
            left join fetch r.reservationSeats rs
            left join fetch rs.performanceSeat ps
            where r.id = :reservationId
            """)
    Optional<Reservation> findByIdForPayment(
            @Param("reservationId") Long reservationId
    );

    @Query("""
            select count(rs)
            from ReservationSeat rs
            join rs.reservation r
            where r.member.id = :memberId
              and r.performance.id = :performanceId
              and r.status in :statuses
            """)
    long countSeatsByMemberAndPerformance(
            @Param("memberId") Long memberId,
            @Param("performanceId") Long performanceId,
            @Param("statuses")
            Collection<ReservationStatus> statuses
    );
}
