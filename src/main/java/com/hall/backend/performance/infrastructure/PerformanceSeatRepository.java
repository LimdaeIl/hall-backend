package com.hall.backend.performance.infrastructure;

import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.performance.domain.PerformanceSeatStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceSeatRepository
        extends JpaRepository<PerformanceSeat, Long> {

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
            order by s.rowNumber asc,
                     s.columnNumber asc,
                     ps.id asc
            """)
    List<PerformanceSeat> findAllByPerformanceIdAndStatus(
            @Param("performanceId") Long performanceId,
            @Param("status") PerformanceSeatStatus status
    );
}
