package com.hall.backend.performance.infrastructure;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.performance.infrastructure.projection.ConcertPerformanceProjection;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    boolean existsByConcertIdAndStartsAt(
            Long concertId,
            LocalDateTime startsAt
    );

    @Query("""
            select
                p.id as performanceId,
                p.startsAt as startsAt,
                p.reservationOpensAt as reservationOpensAt,
                p.reservationClosesAt as reservationClosesAt,
                p.maxTicketsPerMember as maxTicketsPerMember,
                p.status as status,
                sum(
                    case
                        when ps.status =
                            com.hall.backend.performance.domain.PerformanceSeatStatus.AVAILABLE
                        then 1
                        else 0
                    end
                ) as availableSeatCount
            from Performance p
            left join PerformanceSeat ps
                on ps.performance = p
            where p.concert.id = :concertId
              and p.status in :statuses
            group by
                p.id,
                p.startsAt,
                p.reservationOpensAt,
                p.reservationClosesAt,
                p.maxTicketsPerMember,
                p.status
            order by
                p.startsAt asc,
                p.id asc
            """)
    List<ConcertPerformanceProjection> findConcertPerformances(
            @Param("concertId") Long concertId,
            @Param("statuses") Collection<PerformanceStatus> statuses
    );

    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
            delete from Performance p
            where p.concert.id = :concertId
            """)
    int deleteAllByConcertId(
            @Param("concertId") Long concertId
    );
}
