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

    boolean existsByConcertIdAndStartsAtAndIdNot(
            Long concertId,
            LocalDateTime startsAt,
            Long performanceId
    );

    @Query("""
            SELECT p
            FROM Performance p
            JOIN FETCH p.concert
            WHERE p.id = :performanceId
            """)
    Optional<Performance> findByIdWithConcert(
            @Param("performanceId") Long performanceId
    );

    @Query("""
            SELECT
                p.id AS performanceId,
                p.startsAt AS startsAt,
                p.reservationOpensAt AS reservationOpensAt,
                p.reservationClosesAt AS reservationClosesAt,
                p.maxTicketsPerMember AS maxTicketsPerMember,
                p.status AS status,
                SUM(
                    CASE
                        WHEN ps.status =
                            com.hall.backend.performance.domain.PerformanceSeatStatus.AVAILABLE
                        THEN 1
                        ELSE 0
                    END
                ) AS availableSeatCount
            FROM Performance p
            LEFT JOIN PerformanceSeat ps
                ON ps.performance = p
            WHERE p.concert.id = :concertId
              AND p.status IN :statuses
            GROUP BY
                p.id,
                p.startsAt,
                p.reservationOpensAt,
                p.reservationClosesAt,
                p.maxTicketsPerMember,
                p.status
            ORDER BY
                p.startsAt ASC,
                p.id ASC
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
            DELETE FROM Performance p
            WHERE p.concert.id = :concertId
            """)
    int deleteAllByConcertId(
            @Param("concertId") Long concertId
    );

    @Query(
            value = """
                    SELECT p
                    FROM Performance p
                    JOIN FETCH p.concert c
                    WHERE (
                        :concertId IS NULL
                        OR c.id = :concertId
                    )
                      AND (
                          :concertTitle IS NULL
                          OR LOWER(c.title) LIKE LOWER(
                              CONCAT('%', :concertTitle, '%')
                          )
                      )
                      AND (
                          :status IS NULL
                          OR p.status = :status
                      )
                      AND (
                          :startsAtFrom IS NULL
                          OR p.startsAt >= :startsAtFrom
                      )
                      AND (
                          :startsAtTo IS NULL
                          OR p.startsAt <= :startsAtTo
                      )
                      AND (
                          :reservationOpensAtFrom IS NULL
                          OR p.reservationOpensAt >= :reservationOpensAtFrom
                      )
                      AND (
                          :reservationOpensAtTo IS NULL
                          OR p.reservationOpensAt <= :reservationOpensAtTo
                      )
                      AND (
                          :reservationClosesAtFrom IS NULL
                          OR p.reservationClosesAt >= :reservationClosesAtFrom
                      )
                      AND (
                          :reservationClosesAtTo IS NULL
                          OR p.reservationClosesAt <= :reservationClosesAtTo
                      )
                    """,
            countQuery = """
                    SELECT COUNT(p)
                    FROM Performance p
                    JOIN p.concert c
                    WHERE (
                        :concertId IS NULL
                        OR c.id = :concertId
                    )
                      AND (
                          :concertTitle IS NULL
                          OR LOWER(c.title) LIKE LOWER(
                              CONCAT('%', :concertTitle, '%')
                          )
                      )
                      AND (
                          :status IS NULL
                          OR p.status = :status
                      )
                      AND (
                          :startsAtFrom IS NULL
                          OR p.startsAt >= :startsAtFrom
                      )
                      AND (
                          :startsAtTo IS NULL
                          OR p.startsAt <= :startsAtTo
                      )
                      AND (
                          :reservationOpensAtFrom IS NULL
                          OR p.reservationOpensAt >= :reservationOpensAtFrom
                      )
                      AND (
                          :reservationOpensAtTo IS NULL
                          OR p.reservationOpensAt <= :reservationOpensAtTo
                      )
                      AND (
                          :reservationClosesAtFrom IS NULL
                          OR p.reservationClosesAt >= :reservationClosesAtFrom
                      )
                      AND (
                          :reservationClosesAtTo IS NULL
                          OR p.reservationClosesAt <= :reservationClosesAtTo
                      )
                    """
    )
    Page<Performance> searchForAdmin(
            @Param("concertId") Long concertId,
            @Param("concertTitle") String concertTitle,
            @Param("status") PerformanceStatus status,
            @Param("startsAtFrom") LocalDateTime startsAtFrom,
            @Param("startsAtTo") LocalDateTime startsAtTo,
            @Param("reservationOpensAtFrom") LocalDateTime reservationOpensAtFrom,
            @Param("reservationOpensAtTo") LocalDateTime reservationOpensAtTo,
            @Param("reservationClosesAtFrom") LocalDateTime reservationClosesAtFrom,
            @Param("reservationClosesAtTo") LocalDateTime reservationClosesAtTo,
            Pageable pageable
    );
}
