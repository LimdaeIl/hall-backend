package com.hall.backend.performance.domain;

import com.hall.backend.concert.domain.Seat;
import com.hall.backend.concert.domain.SeatGrade;
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

    private PerformanceSeat(Performance performance, Seat seat, SeatGrade grade, long price) {
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

    public static PerformanceSeat create(Performance performance, Seat seat, long price) {
        return new PerformanceSeat(performance, seat, seat.getGrade(), price);
    }

    public static PerformanceSeat create(Performance performance, Seat seat, SeatGrade grade,
            long price) {
        return new PerformanceSeat(performance, seat, grade, price);
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
        validateStatus(PerformanceSeatStatus.AVAILABLE, "예약 가능한 좌석만 선점할 수 있습니다.");
        this.status = PerformanceSeatStatus.HELD;
    }

    public void reserve() {
        validateStatus(PerformanceSeatStatus.HELD, "선점된 좌석만 예약 확정할 수 있습니다.");
        this.status = PerformanceSeatStatus.RESERVED;
    }

    public void release() {
        validateStatus(PerformanceSeatStatus.HELD, "선점된 좌석만 해제할 수 있습니다.");

        this.status = PerformanceSeatStatus.AVAILABLE;
    }

    public void block() {
        validateStatus(PerformanceSeatStatus.AVAILABLE, "예약 가능한 좌석만 판매 차단할 수 있습니다.");

        this.status = PerformanceSeatStatus.BLOCKED;
    }

    public void unblock() {
        validateStatus(PerformanceSeatStatus.BLOCKED, "차단된 좌석만 판매 가능 상태로 변경할 수 있습니다.");

        this.status = PerformanceSeatStatus.AVAILABLE;
    }

    private void validateStatus(PerformanceSeatStatus expected, String message) {
        if (status != expected) {
            throw new IllegalStateException(message);
        }
    }

    private static void validatePerformance(Performance performance) {
        if (performance == null) {
            throw new IllegalArgumentException("공연 회차는 필수입니다.");
        }
    }

    private static void validateSeat(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("좌석은 필수입니다.");
        }
    }

    private static void validateGrade(SeatGrade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("공연 좌석 등급은 필수입니다.");
        }
    }

    private static void validatePrice(long price) {
        if (price < 0) {
            throw new IllegalArgumentException("공연 좌석 가격은 0원 이상이어야 합니다.");
        }
    }
}
