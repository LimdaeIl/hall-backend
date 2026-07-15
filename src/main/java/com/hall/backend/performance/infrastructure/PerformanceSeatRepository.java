package com.hall.backend.performance.infrastructure;

import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceSeatRepository extends JpaRepository<PerformanceSeat, Long> {

    boolean existsByPerformanceId(Long performanceId);

    @Query("""
            SELECT ps
            FROM PerformanceSeat ps
            JOIN FETCH ps.seat s
            WHERE ps.performance.id = :performanceId
            ORDER BY
                s.rowNumber ASC,
                s.columnNumber ASC,
                ps.id ASC
            """)
    List<PerformanceSeat> findAllByPerformanceId(
            @Param("performanceId") Long performanceId
    );

    @Query("""
            SELECT ps
            FROM PerformanceSeat ps
            JOIN FETCH ps.seat s
            WHERE ps.performance.id = :performanceId
              AND ps.status = :status
            ORDER BY
                s.rowNumber ASC,
                s.columnNumber ASC,
                ps.id ASC
            """)
    List<PerformanceSeat> findAllByPerformanceIdAndStatus(
            @Param("performanceId") Long performanceId,
            @Param("status") PerformanceSeatStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT ps
            FROM PerformanceSeat ps
            JOIN FETCH ps.performance
            JOIN FETCH ps.seat
            WHERE ps.id IN :ids
            ORDER BY ps.id ASC
            """)
    List<PerformanceSeat> findAllByIdInForUpdate(
            @Param("ids") List<Long> ids
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT ps
            FROM PerformanceSeat ps
            JOIN FETCH ps.performance p
            JOIN FETCH p.concert
            JOIN FETCH ps.seat
            WHERE ps.id = :performanceSeatId
            """)
    Optional<PerformanceSeat> findByIdForUpdate(
            @Param("performanceSeatId") Long performanceSeatId
    );

    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
            DELETE FROM PerformanceSeat ps
            WHERE ps.performance.id = :performanceId
            """)
    int deleteAllByPerformanceId(
            @Param("performanceId") Long performanceId
    );

    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
            DELETE FROM PerformanceSeat ps
            WHERE ps.performance.concert.id = :concertId
            """)
    int deleteAllByConcertId(
            @Param("concertId") Long concertId
    );
}
