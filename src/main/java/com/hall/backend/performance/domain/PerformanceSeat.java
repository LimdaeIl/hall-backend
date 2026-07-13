package com.hall.backend.performance.domain;

import com.hall.backend.concert.domain.Seat;
import com.hall.backend.concert.domain.SeatGrade;
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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "v1_performance_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_v1_performance_seats_performance_seat",
                        columnNames = {
                                "performance_id",
                                "seat_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_v1_performance_seats_performance_status",
                        columnList = "performance_id, status"
                ),
                @Index(
                        name = "idx_v1_performance_seats_performance_grade",
                        columnList = "performance_id, grade"
                )
        }
)
@Entity
public class PerformanceSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false, length = 20)
    private SeatGrade grade;

    @Column(name = "price", nullable = false)
    private long price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PerformanceSeatStatus status;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    private PerformanceSeat(
            Performance performance,
            Seat seat,
            SeatGrade grade,
            long price
    ) {
        validatePerformance(performance);
        validateSeat(seat);
        validateGrade(grade);
        validatePrice(price);

        this.performance = performance;
        this.seat = seat;
        this.grade = grade;
        this.price = price;
        this.status = PerformanceSeatStatus.AVAILABLE;
    }

    public static PerformanceSeat create(
            Performance performance,
            Seat seat,
            long price
    ) {
        validateSeat(seat);

        return new PerformanceSeat(
                performance,
                seat,
                seat.getGrade(),
                price
        );
    }

    public static PerformanceSeat create(
            Performance performance,
            Seat seat,
            SeatGrade grade,
            long price
    ) {
        return new PerformanceSeat(
                performance,
                seat,
                grade,
                price
        );
    }

    public boolean isAvailable() {
        return status == PerformanceSeatStatus.AVAILABLE;
    }

    public boolean isHeld() {
        return status == PerformanceSeatStatus.HELD;
    }

    public boolean isReserved() {
        return status == PerformanceSeatStatus.RESERVED;
    }

    public boolean isBlocked() {
        return status == PerformanceSeatStatus.BLOCKED;
    }

    public void hold() {
        if (!isAvailable()) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_NOT_AVAILABLE
            );
        }

        this.status = PerformanceSeatStatus.HELD;
    }

    public void reserve() {
        if (!isHeld()) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_NOT_HELD
            );
        }

        this.status = PerformanceSeatStatus.RESERVED;
    }

    public void release() {
        if (!isHeld()) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_NOT_HELD
            );
        }

        this.status = PerformanceSeatStatus.AVAILABLE;
    }

    public void block() {
        if (!isAvailable()) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_NOT_AVAILABLE
            );
        }

        this.status = PerformanceSeatStatus.BLOCKED;
    }

    public void unblock() {
        if (!isBlocked()) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_NOT_BLOCKED
            );
        }

        this.status = PerformanceSeatStatus.AVAILABLE;
    }

    private static void validatePerformance(
            Performance performance
    ) {
        if (performance == null) {
            throw new PerformanceException(
                    PerformanceErrorCode.PERFORMANCE_REQUIRED
            );
        }
    }

    private static void validateSeat(
            Seat seat
    ) {
        if (seat == null) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_REQUIRED
            );
        }
    }

    private static void validateGrade(
            SeatGrade grade
    ) {
        if (grade == null) {
            throw new PerformanceException(
                    PerformanceErrorCode.SEAT_GRADE_REQUIRED
            );
        }
    }

    private static void validatePrice(
            long price
    ) {
        if (price < 0) {
            throw new PerformanceException(
                    PerformanceErrorCode.INVALID_SEAT_PRICE
            );
        }
    }
}
