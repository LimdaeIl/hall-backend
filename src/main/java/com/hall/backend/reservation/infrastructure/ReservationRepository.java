package com.hall.backend.reservation.infrastructure;

import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.infrastructure.projection.AdminReservationProjection;
import com.hall.backend.reservation.infrastructure.projection.MemberReservationProjection;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT DISTINCT r
            FROM Reservation r
            JOIN FETCH r.member
            JOIN FETCH r.performance p
            JOIN FETCH p.concert
            LEFT JOIN FETCH r.reservationSeats rs
            LEFT JOIN FETCH rs.performanceSeat ps
            LEFT JOIN FETCH ps.seat
            WHERE r.id = :reservationId
            """)
    Optional<Reservation> findByIdForPayment(
            @Param("reservationId") Long reservationId
    );

    @Query("""
            SELECT DISTINCT r
            FROM Reservation r
            JOIN FETCH r.member m
            JOIN FETCH r.performance p
            JOIN FETCH p.concert
            LEFT JOIN FETCH r.reservationSeats rs
            LEFT JOIN FETCH rs.performanceSeat ps
            LEFT JOIN FETCH ps.seat
            WHERE r.id = :reservationId
              AND m.id = :memberId
            """)
    Optional<Reservation> findDetailByIdAndMemberId(
            @Param("reservationId") Long reservationId,
            @Param("memberId") Long memberId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT DISTINCT r
            FROM Reservation r
            JOIN FETCH r.member m
            JOIN FETCH r.performance p
            JOIN FETCH p.concert
            LEFT JOIN FETCH r.reservationSeats rs
            LEFT JOIN FETCH rs.performanceSeat ps
            LEFT JOIN FETCH ps.seat
            WHERE r.id = :reservationId
              AND m.id = :memberId
            """)
    Optional<Reservation> findByIdAndMemberIdForCancel(
            @Param("reservationId") Long reservationId,
            @Param("memberId") Long memberId
    );

    @Query(
            value = """
                    SELECT
                        r.id AS reservationId,
                        r.status AS reservationStatus,
                        r.totalAmount AS totalAmount,
                        r.expiresAt AS expiresAt,
                        r.completedAt AS completedAt,
                        r.cancelledAt AS cancelledAt,
                        r.createdAt AS createdAt,
                    
                        p.id AS performanceId,
                        p.startsAt AS startsAt,
                        p.status AS performanceStatus,
                    
                        c.id AS concertId,
                        c.title AS concertTitle,
                        c.artist AS artist,
                    
                        COUNT(rs.id) AS seatCount,
                    
                        pay.id AS paymentId,
                        pay.status AS paymentStatus
                    
                    FROM Reservation r
                    JOIN r.performance p
                    JOIN p.concert c
                    LEFT JOIN r.reservationSeats rs
                    LEFT JOIN Payment pay
                        ON pay.reservation = r
                    
                    WHERE r.member.id = :memberId
                      AND (
                          :status IS NULL
                          OR r.status = :status
                      )
                    
                    GROUP BY
                        r.id,
                        r.status,
                        r.totalAmount,
                        r.expiresAt,
                        r.completedAt,
                        r.cancelledAt,
                        r.createdAt,
                    
                        p.id,
                        p.startsAt,
                        p.status,
                    
                        c.id,
                        c.title,
                        c.artist,
                    
                        pay.id,
                        pay.status
                    """,
            countQuery = """
                    SELECT COUNT(r)
                    FROM Reservation r
                    WHERE r.member.id = :memberId
                      AND (
                          :status IS NULL
                          OR r.status = :status
                      )
                    """
    )
    Page<MemberReservationProjection> findMyReservations(
            @Param("memberId") Long memberId,
            @Param("status") ReservationStatus status,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT
                        r.id AS reservationId,
                        r.status AS reservationStatus,
                        r.totalAmount AS totalAmount,
                        r.expiresAt AS expiresAt,
                        r.completedAt AS completedAt,
                        r.cancelledAt AS cancelledAt,
                        r.createdAt AS createdAt,
                    
                        m.id AS memberId,
                        m.email AS memberEmail,
                    
                        p.id AS performanceId,
                        p.startsAt AS startsAt,
                        p.status AS performanceStatus,
                    
                        c.id AS concertId,
                        c.title AS concertTitle,
                        c.artist AS artist,
                    
                        COUNT(rs.id) AS seatCount,
                    
                        pay.id AS paymentId,
                        pay.method AS paymentMethod,
                        pay.status AS paymentStatus,
                        pay.transactionKey AS transactionKey,
                        pay.paidAt AS paidAt,
                        pay.cancelledAt AS paymentCancelledAt
                    
                    FROM Reservation r
                    JOIN r.member m
                    JOIN r.performance p
                    JOIN p.concert c
                    LEFT JOIN r.reservationSeats rs
                    LEFT JOIN Payment pay
                        ON pay.reservation = r
                    
                    WHERE (
                        :memberId IS NULL
                        OR m.id = :memberId
                    )
                      AND (
                          :memberEmail IS NULL
                          OR LOWER(m.email) LIKE LOWER(
                              CONCAT('%', :memberEmail, '%')
                          )
                      )
                      AND (
                          :concertId IS NULL
                          OR c.id = :concertId
                      )
                      AND (
                          :performanceId IS NULL
                          OR p.id = :performanceId
                      )
                      AND (
                          :reservationStatus IS NULL
                          OR r.status = :reservationStatus
                      )
                      AND (
                          :paymentStatus IS NULL
                          OR pay.status = :paymentStatus
                      )
                      AND (
                          :createdFrom IS NULL
                          OR r.createdAt >= :createdFrom
                      )
                      AND (
                          :createdTo IS NULL
                          OR r.createdAt < :createdTo
                      )
                    
                    GROUP BY
                        r.id,
                        r.status,
                        r.totalAmount,
                        r.expiresAt,
                        r.completedAt,
                        r.cancelledAt,
                        r.createdAt,
                    
                        m.id,
                        m.email,
                    
                        p.id,
                        p.startsAt,
                        p.status,
                    
                        c.id,
                        c.title,
                        c.artist,
                    
                        pay.id,
                        pay.method,
                        pay.status,
                        pay.transactionKey,
                        pay.paidAt,
                        pay.cancelledAt
                    """,
            countQuery = """
                    SELECT COUNT(r)
                    FROM Reservation r
                    JOIN r.member m
                    JOIN r.performance p
                    JOIN p.concert c
                    LEFT JOIN Payment pay
                        ON pay.reservation = r
                    
                    WHERE (
                        :memberId IS NULL
                        OR m.id = :memberId
                    )
                      AND (
                          :memberEmail IS NULL
                          OR LOWER(m.email) LIKE LOWER(
                              CONCAT('%', :memberEmail, '%')
                          )
                      )
                      AND (
                          :concertId IS NULL
                          OR c.id = :concertId
                      )
                      AND (
                          :performanceId IS NULL
                          OR p.id = :performanceId
                      )
                      AND (
                          :reservationStatus IS NULL
                          OR r.status = :reservationStatus
                      )
                      AND (
                          :paymentStatus IS NULL
                          OR pay.status = :paymentStatus
                      )
                      AND (
                          :createdFrom IS NULL
                          OR r.createdAt >= :createdFrom
                      )
                      AND (
                          :createdTo IS NULL
                          OR r.createdAt < :createdTo
                      )
                    """
    )
    Page<AdminReservationProjection> searchForAdmin(
            @Param("memberId") Long memberId,
            @Param("memberEmail") String memberEmail,
            @Param("concertId") Long concertId,
            @Param("performanceId") Long performanceId,
            @Param("reservationStatus") ReservationStatus reservationStatus,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("createdFrom") Instant createdFrom,
            @Param("createdTo") Instant createdTo,
            Pageable pageable
    );

    @Query("""
            SELECT COUNT(rs)
            FROM ReservationSeat rs
            JOIN rs.reservation r
            WHERE r.member.id = :memberId
              AND r.performance.id = :performanceId
              AND r.status IN :statuses
            """)
    long countSeatsByMemberAndPerformance(
            @Param("memberId") Long memberId,
            @Param("performanceId") Long performanceId,
            @Param("statuses") Collection<ReservationStatus> statuses
    );

    boolean existsByPerformance_Concert_Id(Long concertId);
}
