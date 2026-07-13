package com.hall.backend.reservation.domain;

import com.hall.backend.member.domain.Member;
import com.hall.backend.performance.domain.Performance;
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

    @Column(nullable = false, unique = true, length = 30)
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

    @Column(nullable = false)
    private long totalAmount;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @OneToMany(
            mappedBy = "reservation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<ReservationSeat> reservationSeats =
            new ArrayList<>();

    public boolean isExpired(LocalDateTime now) {
        return status == ReservationStatus.PENDING_PAYMENT
                && !now.isBefore(expiredAt);
    }

    public void complete() {
        if (status != ReservationStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("결제 대기 예약만 완료할 수 있습니다.");
        }

        this.status = ReservationStatus.COMPLETED;
    }

    public void cancel() {
        if (status == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("완료된 예약은 별도 취소 정책이 필요합니다.");
        }

        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        if (status != ReservationStatus.PENDING_PAYMENT) {
            return;
        }

        this.status = ReservationStatus.EXPIRED;
    }
}
