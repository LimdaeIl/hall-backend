package com.hall.backend.reservation.domain;

import com.hall.backend.common.persistence.entity.BaseAuditEntity;
import com.hall.backend.member.domain.Member;
import com.hall.backend.performance.domain.Performance;
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
import jakarta.persistence.Version;
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
public class Reservation extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSeat> reservationSeats = new ArrayList<>();

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ReservationStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    private Reservation(
            Member member,
            Performance performance,
            LocalDateTime expiresAt
    ) {
        validateMember(member);
        validatePerformance(performance);
        validateExpiresAt(expiresAt);

        this.member = member;
        this.performance = performance;
        this.expiresAt = expiresAt;
        this.status = ReservationStatus.PENDING_PAYMENT;
        this.totalAmount = 0L;
    }

    public static Reservation create(
            Member member,
            Performance performance,
            LocalDateTime expiresAt
    ) {
        return new Reservation(
                member,
                performance,
                expiresAt
        );
    }

    public void addSeat(ReservationSeat reservationSeat) {
        if (reservationSeat == null) {
            throw new ReservationException(ReservationErrorCode.SEAT_REQUIRED);
        }

        if (reservationSeat.getReservation() != this) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_SEAT);
        }

        boolean duplicated = reservationSeats.stream()
                .anyMatch(existing ->
                        existing.getPerformanceSeat()
                                .getId()
                                .equals(
                                        reservationSeat
                                                .getPerformanceSeat()
                                                .getId()
                                )
                );

        if (duplicated) {
            throw new ReservationException(ReservationErrorCode.DUPLICATE_SEAT);
        }

        reservationSeats.add(reservationSeat);

        try {
            totalAmount = Math.addExact(
                    totalAmount,
                    reservationSeat.getPrice()
            );
        } catch (ArithmeticException exception) {
            throw new ReservationException(ReservationErrorCode.TOTAL_AMOUNT_OVERFLOW);
        }
    }

    public void complete(LocalDateTime completedAt) {
        validatePendingPayment();

        if (completedAt == null) {
            throw new ReservationException(ReservationErrorCode.COMPLETED_AT_REQUIRED);
        }

        if (completedAt.isAfter(expiresAt)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_EXPIRED);
        }

        reservationSeats.forEach(ReservationSeat::complete);

        this.status = ReservationStatus.COMPLETED;
        this.completedAt = completedAt;
    }

    public void cancel(LocalDateTime cancelledAt) {
        if (status != ReservationStatus.PENDING_PAYMENT
                && status != ReservationStatus.COMPLETED) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_STATUS);
        }

        if (cancelledAt == null) {
            throw new ReservationException(ReservationErrorCode.CANCELLED_AT_REQUIRED);
        }

        reservationSeats.forEach(ReservationSeat::cancel);

        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
    }

    public void expire(LocalDateTime now) {
        validatePendingPayment();

        if (now == null) {
            throw new ReservationException(ReservationErrorCode.CURRENT_TIME_REQUIRED);
        }

        if (now.isBefore(expiresAt)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_EXPIRED);
        }

        reservationSeats.forEach(ReservationSeat::cancel);
        this.status = ReservationStatus.EXPIRED;
    }

    public boolean isOwnedBy(Long memberId) {
        return memberId != null && member.getId().equals(memberId);
    }

    public boolean isPendingPayment() {
        return status == ReservationStatus.PENDING_PAYMENT;
    }

    public boolean isExpired(LocalDateTime now) {
        return now != null && !now.isBefore(expiresAt);
    }

    public List<ReservationSeat> getReservationSeats() {
        return Collections.unmodifiableList(reservationSeats);
    }

    private void validatePendingPayment() {
        if (status != ReservationStatus.PENDING_PAYMENT) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_STATUS);
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

    private static void validateExpiresAt(
            LocalDateTime expiresAt
    ) {
        if (expiresAt == null) {
            throw new ReservationException(ReservationErrorCode.EXPIRES_AT_REQUIRED);
        }
    }
}
