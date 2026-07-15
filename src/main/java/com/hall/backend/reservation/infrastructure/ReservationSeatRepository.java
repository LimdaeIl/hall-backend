package com.hall.backend.reservation.infrastructure;

import com.hall.backend.reservation.domain.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationSeatRepository
        extends JpaRepository<ReservationSeat, Long> {

    boolean existsByPerformanceSeat_Id(Long performanceSeatId);

    boolean existsByPerformanceSeat_Performance_Id(Long performanceId);

    @Query("""
        SELECT COUNT(rs) > 0
        FROM ReservationSeat rs
        WHERE rs.performanceSeat.performance.id = :performanceId
        """)
    boolean existsReservationHistoryByPerformanceId(
            @Param("performanceId") Long performanceId
    );
}
