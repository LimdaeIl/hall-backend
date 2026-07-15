package com.hall.backend.reservation.infrastructure;

import com.hall.backend.reservation.domain.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationSeatRepository
        extends JpaRepository<ReservationSeat, Long> {

    boolean existsByPerformanceSeat_Id(
            Long performanceSeatId
    );

    boolean existsByPerformanceSeat_Performance_Id(
            Long performanceId
    );

    @Query("""
        select case
            when count(rs) > 0 then true
            else false
        end
        from ReservationSeat rs
        where rs.performanceSeat.performance.id =
            :performanceId
        """)
    boolean existsReservationHistoryByPerformanceId(
            @Param("performanceId")
            Long performanceId
    );
}
