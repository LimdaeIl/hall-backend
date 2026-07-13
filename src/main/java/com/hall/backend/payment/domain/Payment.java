package com.hall.backend.payment.domain;

import com.hall.backend.reservation.domain.Reservation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "transaction_key", length = 100)
    private String transactionKey;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    public void approve(
            String transactionKey,
            LocalDateTime paidAt
    ) {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태가 아닙니다.");
        }

        this.transactionKey = transactionKey;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = paidAt;
    }

    public void fail() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("결제 대기 상태가 아닙니다.");
        }

        this.status = PaymentStatus.FAILED;
    }

    public void cancel(LocalDateTime cancelledAt) {
        if (status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("완료된 결제만 취소할 수 있습니다.");
        }

        this.status = PaymentStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
    }
}