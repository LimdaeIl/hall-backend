package com.hall.backend.reservation.domain;

import com.hall.backend.member.domain.Member;
import com.hall.backend.performance.domain.Performance;
import com.hall.backend.performance.domain.PerformanceSeat;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_reservations")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "reservation_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @OneToMany(
            mappedBy = "reservation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<ReservationSeat> reservationSeats =
            new ArrayList<>();

    private Reservation(
            String reservationNumber,
            Member member,
            Performance performance,
            LocalDateTime expiredAt
    ) {
        validateReservationNumber(reservationNumber);
        validateMember(member);
        validatePerformance(performance);
        validateExpiredAt(expiredAt);

        this.reservationNumber = reservationNumber;
        this.member = member;
        this.performance = performance;
        this.status = ReservationStatus.PENDING_PAYMENT;
        this.totalAmount = 0L;
        this.expiredAt = expiredAt;
    }

    public static Reservation create(
            String reservationNumber,
            Member member,
            Performance performance,
            LocalDateTime expiredAt
    ) {
        return new Reservation(
                reservationNumber,
                member,
                performance,
                expiredAt
        );
    }

    public void addSeat(PerformanceSeat performanceSeat) {
        validatePendingPayment();
        validateSamePerformance(performanceSeat);

        ReservationSeat reservationSeat =
                ReservationSeat.create(this, performanceSeat);

        reservationSeats.add(reservationSeat);
        totalAmount += performanceSeat.getPrice();
    }

    public List<ReservationSeat> getReservationSeats() {
        return Collections.unmodifiableList(reservationSeats);
    }

    public boolean isExpired(LocalDateTime now) {
        return status == ReservationStatus.PENDING_PAYMENT
                && !now.isBefore(expiredAt);
    }

    public void complete(
            LocalDateTime now
    ) {
        validatePendingPayment();
        validateHasSeats();

        if (isExpired(now)) {
            throw new ReservationException(
                    ReservationErrorCode.RESERVATION_EXPIRED
            );
        }

        reservationSeats.forEach(
                ReservationSeat::complete
        );

        this.status = ReservationStatus.COMPLETED;
    }

    private void validateHasSeats() {
        if (reservationSeats.isEmpty()) {
            throw new ReservationException(
                    ReservationErrorCode.RESERVATION_SEAT_REQUIRED
            );
        }
    }

    public void cancel() {
        validatePendingPayment();

        reservationSeats.forEach(
                ReservationSeat::release
        );

        this.status = ReservationStatus.CANCELLED;
    }

    public void expire(
            LocalDateTime now
    ) {
        validatePendingPayment();

        if (!isExpired(now)) {
            throw new ReservationException(
                    ReservationErrorCode.RESERVATION_NOT_EXPIRED
            );
        }

        reservationSeats.forEach(
                ReservationSeat::release
        );

        this.status = ReservationStatus.EXPIRED;
    }

    private void validatePendingPayment() {
        if (status != ReservationStatus.PENDING_PAYMENT) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_PENDING_PAYMENT);
        }
    }


    private void validateSamePerformance(
            PerformanceSeat performanceSeat
    ) {
        if (performanceSeat == null) {
            throw new ReservationException(ReservationErrorCode.PERFORMANCE_SEAT_REQUIRED);
        }

        Long reservationPerformanceId = performance.getId();
        Long seatPerformanceId =
                performanceSeat.getPerformance().getId();

        if (!reservationPerformanceId.equals(seatPerformanceId)) {
            throw new ReservationException(ReservationErrorCode.INVALID_PERFORMANCE_SEAT);
            }
    }

    private static void validateReservationNumber(
            String reservationNumber
    ) {
        if (reservationNumber == null
                || reservationNumber.isBlank()) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NUMBER_REQUIRED);
        }

        if (reservationNumber.length() > 30) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NUMBER_TOO_LONG);
        }
    }

    private static void validateMember(Member member) {
        if (member == null) {
            throw new ReservationException(ReservationErrorCode.MEMBER_REQUIRED);
        }
    }

    private static void validatePerformance(
            Performance performance
    ) {
        if (performance == null) {
            throw new ReservationException(ReservationErrorCode.PERFORMANCE_REQUIRED);
        }
    }

    private static void validateExpiredAt(
            LocalDateTime expiredAt
    ) {
        if (expiredAt == null) {
            throw new ReservationException(ReservationErrorCode.EXPIRED_AT_REQUIRED);
        }
    }

    public boolean isOwnedBy(Long memberId) {
        return member.getId().equals(memberId);
    }



}