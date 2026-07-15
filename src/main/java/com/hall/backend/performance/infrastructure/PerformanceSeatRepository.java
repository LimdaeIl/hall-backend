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

public interface PerformanceSeatRepository
        extends JpaRepository<PerformanceSeat, Long> {

    boolean existsByPerformanceId(
            Long performanceId
    );

    boolean existsByPerformanceIdAndSeatId(
            Long performanceId,
            Long seatId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select ps
            from PerformanceSeat ps
            join fetch ps.performance
            join fetch ps.seat
            where ps.id in :ids
            order by ps.id
            """)
    List<PerformanceSeat> findAllByIdInForUpdate(
            @Param("ids") List<Long> ids
    );

    @Query("""
            select ps
            from PerformanceSeat ps
            join fetch ps.seat s
            where ps.performance.id = :performanceId
              and ps.status = :status
            order by
                s.rowNumber asc,
                s.columnNumber asc,
                ps.id asc
            """)
    List<PerformanceSeat> findAllByPerformanceIdAndStatus(
            @Param("performanceId")
            Long performanceId,

            @Param("status")
            PerformanceSeatStatus status
    );

    @Query("""
            select ps
            from PerformanceSeat ps
            join fetch ps.seat s
            where ps.performance.id = :performanceId
            order by
                s.rowNumber asc,
                s.columnNumber asc,
                ps.id asc
            """)
    List<PerformanceSeat> findAllByPerformanceId(
            @Param("performanceId")
            Long performanceId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select ps
            from PerformanceSeat ps
            join fetch ps.performance p
            join fetch p.concert
            join fetch ps.seat
            where ps.id = :performanceSeatId
            """)
    Optional<PerformanceSeat> findByIdForUpdate(
            @Param("performanceSeatId")
            Long performanceSeatId
    );

    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
            delete from PerformanceSeat ps
            where ps.performance.concert.id = :concertId
            """)
    int deleteAllByConcertId(
            @Param("concertId")
            Long concertId
    );

    @Modifying(flushAutomatically = true)
    @Query("""
        delete from PerformanceSeat ps
        where ps.performance.id = :performanceId
        """)
    int deleteAllByPerformanceId(
            @Param("performanceId")
            Long performanceId
    );
}
