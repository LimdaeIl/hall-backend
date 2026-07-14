package com.hall.backend.performance.domain;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.performance.exception.PerformanceErrorCode;
import com.hall.backend.performance.exception.PerformanceException;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_performances")
@Entity
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "reservation_opens_at", nullable = false)
    private LocalDateTime reservationOpensAt;

    @Column(name = "reservation_closes_at", nullable = false)
    private LocalDateTime reservationClosesAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PerformanceStatus status;

    private Performance(Concert concert, LocalDateTime startsAt, LocalDateTime reservationOpensAt,
            LocalDateTime reservationClosesAt) {
        validateConcert(concert);
        validateStartsAt(startsAt);
        validateReservationOpensAt(reservationOpensAt);
        validateReservationClosesAt(reservationClosesAt);
        validateReservationPeriod(startsAt, reservationOpensAt, reservationClosesAt);

        this.concert = concert;
        this.startsAt = startsAt;
        this.reservationOpensAt = reservationOpensAt;
        this.reservationClosesAt = reservationClosesAt;
        this.status = PerformanceStatus.PREPARING;
    }

    public static Performance create(Concert concert, LocalDateTime startsAt,
            LocalDateTime reservationOpensAt, LocalDateTime reservationClosesAt) {
        return new Performance(concert, startsAt, reservationOpensAt, reservationClosesAt);
    }

    public boolean isReservable(LocalDateTime now) {
        validateCurrentTime(now);

        return status == PerformanceStatus.OPEN
                && !now.isBefore(reservationOpensAt)
                && now.isBefore(reservationClosesAt);
    }

    public void validateReservable(LocalDateTime now) {
        if (!isReservable(now)) {
            throw new PerformanceException(PerformanceErrorCode.PERFORMANCE_NOT_RESERVABLE);
        }
    }

    private static void validateConcert(Concert concert) {
        if (concert == null) {
            throw new PerformanceException(PerformanceErrorCode.CONCERT_REQUIRED);
        }
    }

    private static void validateStartsAt(LocalDateTime startsAt) {
        if (startsAt == null) {
            throw new PerformanceException(PerformanceErrorCode.STARTS_AT_REQUIRED);
        }
    }

    private static void validateReservationOpensAt(LocalDateTime reservationOpensAt) {
        if (reservationOpensAt == null) {
            throw new PerformanceException(PerformanceErrorCode.RESERVATION_OPENS_AT_REQUIRED);
        }
    }

    private static void validateReservationClosesAt(LocalDateTime reservationClosesAt) {
        if (reservationClosesAt == null) {
            throw new PerformanceException(PerformanceErrorCode.RESERVATION_CLOSES_AT_REQUIRED);
        }
    }

    private static void validateReservationPeriod(LocalDateTime startsAt,
            LocalDateTime reservationOpensAt, LocalDateTime reservationClosesAt) {
        if (!reservationOpensAt.isBefore(reservationClosesAt)) {
            throw new PerformanceException(PerformanceErrorCode.INVALID_RESERVATION_PERIOD);
        }

        if (!reservationClosesAt.isBefore(startsAt)) {
            throw new PerformanceException(
                    PerformanceErrorCode.RESERVATION_MUST_CLOSE_BEFORE_START);
        }
    }

    private static void validateCurrentTime(LocalDateTime now) {
        if (now == null) {
            throw new PerformanceException(PerformanceErrorCode.CURRENT_TIME_REQUIRED);
        }
    }

    public void open() {
        validateStatus(PerformanceStatus.PREPARING);
        this.status = PerformanceStatus.OPEN;
    }

    public void close() {
        if (status != PerformanceStatus.OPEN && status != PerformanceStatus.SOLD_OUT) {
            throw new PerformanceException(PerformanceErrorCode.INVALID_PERFORMANCE_STATUS);
        }
        this.status = PerformanceStatus.CLOSED;
    }

    public void cancel() {
        if (status == PerformanceStatus.CANCELLED || status == PerformanceStatus.COMPLETED) {
            throw new PerformanceException(PerformanceErrorCode.INVALID_PERFORMANCE_STATUS);
        }

        this.status = PerformanceStatus.CANCELLED;
    }

    public void complete() {
        if (status != PerformanceStatus.CLOSED) {
            throw new PerformanceException(PerformanceErrorCode.INVALID_PERFORMANCE_STATUS);
        }

        this.status = PerformanceStatus.COMPLETED;
    }

    private void validateStatus(PerformanceStatus expected) {
        if (status != expected) {
            throw new PerformanceException(PerformanceErrorCode.INVALID_PERFORMANCE_STATUS);
        }
    }
}

