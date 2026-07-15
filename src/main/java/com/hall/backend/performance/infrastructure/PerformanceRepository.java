package com.hall.backend.performance.infrastructure;

import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.performance.infrastructure.projection.ConcertPerformanceProjection;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    boolean existsByConcertIdAndStartsAtAndIdNot(
            Long concertId,
            LocalDateTime startsAt,
            Long performanceId
    );

    @Query("""
        select p
        from Performance p
        join fetch p.concert c
        where p.id = :performanceId
        """)
    Optional<Performance> findByIdWithConcert(
            @Param("performanceId")
            Long performanceId
    );

    @Query(
            value = """
                select p
                from Performance p
                join fetch p.concert c
                where (
                        :concertId is null
                        or c.id = :concertId
                      )
                  and (
                        :concertTitle is null
                        or lower(c.title) like lower(
                            concat('%', :concertTitle, '%')
                        )
                      )
                  and (
                        :status is null
                        or p.status = :status
                      )
                  and (
                        :startsAtFrom is null
                        or p.startsAt >= :startsAtFrom
                      )
                  and (
                        :startsAtTo is null
                        or p.startsAt <= :startsAtTo
                      )
                  and (
                        :reservationOpensAtFrom is null
                        or p.reservationOpensAt >=
                            :reservationOpensAtFrom
                      )
                  and (
                        :reservationOpensAtTo is null
                        or p.reservationOpensAt <=
                            :reservationOpensAtTo
                      )
                  and (
                        :reservationClosesAtFrom is null
                        or p.reservationClosesAt >=
                            :reservationClosesAtFrom
                      )
                  and (
                        :reservationClosesAtTo is null
                        or p.reservationClosesAt <=
                            :reservationClosesAtTo
                      )
                """,
            countQuery = """
                select count(p)
                from Performance p
                join p.concert c
                where (
                        :concertId is null
                        or c.id = :concertId
                      )
                  and (
                        :concertTitle is null
                        or lower(c.title) like lower(
                            concat('%', :concertTitle, '%')
                        )
                      )
                  and (
                        :status is null
                        or p.status = :status
                      )
                  and (
                        :startsAtFrom is null
                        or p.startsAt >= :startsAtFrom
                      )
                  and (
                        :startsAtTo is null
                        or p.startsAt <= :startsAtTo
                      )
                  and (
                        :reservationOpensAtFrom is null
                        or p.reservationOpensAt >=
                            :reservationOpensAtFrom
                      )
                  and (
                        :reservationOpensAtTo is null
                        or p.reservationOpensAt <=
                            :reservationOpensAtTo
                      )
                  and (
                        :reservationClosesAtFrom is null
                        or p.reservationClosesAt >=
                            :reservationClosesAtFrom
                      )
                  and (
                        :reservationClosesAtTo is null
                        or p.reservationClosesAt <=
                            :reservationClosesAtTo
                      )
                """
    )
    Page<Performance> searchForAdmin(
            @Param("concertId")
            Long concertId,

            @Param("concertTitle")
            String concertTitle,

            @Param("status")
            PerformanceStatus status,

            @Param("startsAtFrom")
            LocalDateTime startsAtFrom,

            @Param("startsAtTo")
            LocalDateTime startsAtTo,

            @Param("reservationOpensAtFrom")
            LocalDateTime reservationOpensAtFrom,

            @Param("reservationOpensAtTo")
            LocalDateTime reservationOpensAtTo,

            @Param("reservationClosesAtFrom")
            LocalDateTime reservationClosesAtFrom,

            @Param("reservationClosesAtTo")
            LocalDateTime reservationClosesAtTo,

            Pageable pageable
    );
}
