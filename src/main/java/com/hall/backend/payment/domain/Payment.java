package com.hall.backend.payment.domain;

import com.hall.backend.payment.exception.PaymentErrorCode;
import com.hall.backend.payment.exception.PaymentException;
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
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @Column(name = "transaction_key", length = 100, unique = true)
    private String transactionKey;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    private Payment(Reservation reservation, long amount, PaymentMethod method) {
        validateReservation(reservation);
        validateAmount(amount);
        validateMethod(method);

        this.reservation = reservation;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment create(Reservation reservation, long amount, PaymentMethod method) {
        return new Payment(reservation, amount, method);
    }

    public void approve(String transactionKey, LocalDateTime paidAt) {
        validatePending();

        if (transactionKey == null || transactionKey.isBlank()) {
            throw new PaymentException(PaymentErrorCode.TRANSACTION_KEY_REQUIRED);
        }

        if (paidAt == null) {
            throw new PaymentException(PaymentErrorCode.PAID_AT_REQUIRED);
        }

        this.transactionKey = transactionKey;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = paidAt;
    }

    public void fail() {
        validatePending();
        this.status = PaymentStatus.FAILED;
    }

    public void cancel(LocalDateTime cancelledAt) {
        if (status != PaymentStatus.COMPLETED) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_COMPLETED);
        }

        if (cancelledAt == null) {
            throw new PaymentException(PaymentErrorCode.CANCELLED_AT_REQUIRED);
        }

        this.status = PaymentStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
    }

    private void validatePending() {
        if (status != PaymentStatus.PENDING) {
            throw new PaymentException(PaymentErrorCode.INVALID_PAYMENT_STATUS);
        }
    }

    private static void validateReservation(
            Reservation reservation
    ) {
        if (reservation == null) {
            throw new PaymentException(PaymentErrorCode.RESERVATION_REQUIRED);
        }
    }

    private static void validateAmount(long amount) {
        if (amount <= 0) {
            throw new PaymentException(PaymentErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }

    private static void validateMethod(
            PaymentMethod method
    ) {
        if (method == null) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_METHOD_REQUIRED);
        }
    }
}
