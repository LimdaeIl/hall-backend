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

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select distinct r
            from Reservation r
            join fetch r.member
            join fetch r.performance p
            join fetch p.concert
            left join fetch r.reservationSeats rs
            left join fetch rs.performanceSeat ps
            left join fetch ps.seat
            where r.id = :reservationId
            """)
    Optional<Reservation> findByIdForPayment(
            @Param("reservationId")
            Long reservationId
    );

    @Query("""
            select distinct r
            from Reservation r
            join fetch r.member m
            join fetch r.performance p
            join fetch p.concert
            left join fetch r.reservationSeats rs
            left join fetch rs.performanceSeat ps
            left join fetch ps.seat
            where r.id = :reservationId
              and m.id = :memberId
            """)
    Optional<Reservation> findDetailByIdAndMemberId(
            @Param("reservationId")
            Long reservationId,

            @Param("memberId")
            Long memberId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select distinct r
            from Reservation r
            join fetch r.member m
            join fetch r.performance p
            join fetch p.concert
            left join fetch r.reservationSeats rs
            left join fetch rs.performanceSeat ps
            left join fetch ps.seat
            where r.id = :reservationId
              and m.id = :memberId
            """)
    Optional<Reservation> findByIdAndMemberIdForCancel(
            @Param("reservationId")
            Long reservationId,

            @Param("memberId")
            Long memberId
    );

    @Query(
            value = """
                    select
                        r.id as reservationId,
                        r.status as reservationStatus,
                        r.totalAmount as totalAmount,
                        r.expiresAt as expiresAt,
                        r.completedAt as completedAt,
                        r.cancelledAt as cancelledAt,
                        r.createdAt as createdAt,

                        p.id as performanceId,
                        p.startsAt as startsAt,
                        p.status as performanceStatus,

                        c.id as concertId,
                        c.title as concertTitle,
                        c.artist as artist,

                        count(rs.id) as seatCount,

                        pay.id as paymentId,
                        pay.status as paymentStatus

                    from Reservation r
                    join r.performance p
                    join p.concert c
                    left join r.reservationSeats rs
                    left join Payment pay
                        on pay.reservation = r

                    where r.member.id = :memberId
                      and (
                            :status is null
                            or r.status = :status
                          )

                    group by
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
                    select count(r)
                    from Reservation r
                    where r.member.id = :memberId
                      and (
                            :status is null
                            or r.status = :status
                          )
                    """
    )
    Page<MemberReservationProjection> findMyReservations(
            @Param("memberId")
            Long memberId,

            @Param("status")
            ReservationStatus status,

            Pageable pageable
    );

    @Query(
            value = """
                    select
                        r.id as reservationId,
                        r.status as reservationStatus,
                        r.totalAmount as totalAmount,
                        r.expiresAt as expiresAt,
                        r.completedAt as completedAt,
                        r.cancelledAt as cancelledAt,
                        r.createdAt as createdAt,

                        m.id as memberId,
                        m.email as memberEmail,

                        p.id as performanceId,
                        p.startsAt as startsAt,
                        p.status as performanceStatus,

                        c.id as concertId,
                        c.title as concertTitle,
                        c.artist as artist,

                        count(rs.id) as seatCount,

                        pay.id as paymentId,
                        pay.method as paymentMethod,
                        pay.status as paymentStatus,
                        pay.transactionKey as transactionKey,
                        pay.paidAt as paidAt,
                        pay.cancelledAt as paymentCancelledAt

                    from Reservation r
                    join r.member m
                    join r.performance p
                    join p.concert c
                    left join r.reservationSeats rs
                    left join Payment pay
                        on pay.reservation = r

                    where (
                            :memberId is null
                            or m.id = :memberId
                          )
                      and (
                            :memberEmail is null
                            or lower(m.email) like lower(
                                concat('%', :memberEmail, '%')
                            )
                          )
                      and (
                            :concertId is null
                            or c.id = :concertId
                          )
                      and (
                            :performanceId is null
                            or p.id = :performanceId
                          )
                      and (
                            :reservationStatus is null
                            or r.status = :reservationStatus
                          )
                      and (
                            :paymentStatus is null
                            or pay.status = :paymentStatus
                          )
                      and (
                            :createdFrom is null
                            or r.createdAt >= :createdFrom
                          )
                      and (
                            :createdTo is null
                            or r.createdAt < :createdTo
                          )

                    group by
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
                    select count(r)
                    from Reservation r
                    join r.member m
                    join r.performance p
                    join p.concert c
                    left join Payment pay
                        on pay.reservation = r

                    where (
                            :memberId is null
                            or m.id = :memberId
                          )
                      and (
                            :memberEmail is null
                            or lower(m.email) like lower(
                                concat('%', :memberEmail, '%')
                            )
                          )
                      and (
                            :concertId is null
                            or c.id = :concertId
                          )
                      and (
                            :performanceId is null
                            or p.id = :performanceId
                          )
                      and (
                            :reservationStatus is null
                            or r.status = :reservationStatus
                          )
                      and (
                            :paymentStatus is null
                            or pay.status = :paymentStatus
                          )
                      and (
                            :createdFrom is null
                            or r.createdAt >= :createdFrom
                          )
                      and (
                            :createdTo is null
                            or r.createdAt < :createdTo
                          )
                    """
    )
    Page<AdminReservationProjection> searchForAdmin(
            @Param("memberId")
            Long memberId,

            @Param("memberEmail")
            String memberEmail,

            @Param("concertId")
            Long concertId,

            @Param("performanceId")
            Long performanceId,

            @Param("reservationStatus")
            ReservationStatus reservationStatus,

            @Param("paymentStatus")
            PaymentStatus paymentStatus,

            @Param("createdFrom")
            Instant createdFrom,

            @Param("createdTo")
            Instant createdTo,

            Pageable pageable
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
            @Param("memberId")
            Long memberId,

            @Param("performanceId")
            Long performanceId,

            @Param("statuses")
            Collection<ReservationStatus> statuses
    );

    boolean existsByPerformance_Concert_Id(
            Long concertId
    );
}
