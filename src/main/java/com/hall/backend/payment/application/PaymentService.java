package com.hall.backend.payment.application;

import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.domain.PaymentMethod;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.payment.exception.PaymentErrorCode;
import com.hall.backend.payment.exception.PaymentException;
import com.hall.backend.payment.infrastructure.PaymentRepository;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    @Transactional
    public Payment pay(
            Long memberId,
            Long reservationId,
            PaymentMethod method
    ) {
        validatePaymentMethod(method);

        Reservation reservation =
                reservationRepository
                        .findByIdForPayment(reservationId)
                        .orElseThrow(() ->
                                new ReservationException(
                                        ReservationErrorCode
                                                .RESERVATION_NOT_FOUND
                                )
                        );

        validateOwner(
                reservation,
                memberId
        );

        Payment existingPayment =
                paymentRepository
                        .findByReservationId(reservationId)
                        .orElse(null);

        if (existingPayment != null) {
            return handleExistingPayment(
                    existingPayment
            );
        }

        LocalDateTime now =
                LocalDateTime.now(clock);

        validatePayableReservation(
                reservation,
                now
        );

        Payment payment = Payment.create(
                reservation,
                reservation.getTotalAmount(),
                method
        );

        payment.approve(
                generateTransactionKey(),
                now
        );

        reservation.complete(now);

        return paymentRepository.save(payment);
    }

    private void validatePayableReservation(
            Reservation reservation,
            LocalDateTime now
    ) {
        if (reservation.isExpired(now)) {
            throw new ReservationException(
                    ReservationErrorCode
                            .RESERVATION_EXPIRED
            );
        }

        if (!reservation.isPendingPayment()) {
            throw new PaymentException(
                    PaymentErrorCode
                            .INVALID_RESERVATION_STATUS
            );
        }
    }

    private void validateOwner(
            Reservation reservation,
            Long memberId
    ) {
        if (!reservation.isOwnedBy(memberId)) {
            throw new PaymentException(
                    PaymentErrorCode
                            .PAYMENT_ACCESS_DENIED
            );
        }
    }

    private void validatePaymentMethod(
            PaymentMethod method
    ) {
        if (method == null) {
            throw new PaymentException(
                    PaymentErrorCode
                            .PAYMENT_METHOD_REQUIRED
            );
        }

        if (method != PaymentMethod.MOCK) {
            throw new PaymentException(
                    PaymentErrorCode
                            .UNSUPPORTED_PAYMENT_METHOD
            );
        }
    }

    private Payment handleExistingPayment(
            Payment payment
    ) {
        if (payment.getStatus()
                == PaymentStatus.COMPLETED) {
            return payment;
        }

        throw new PaymentException(
                PaymentErrorCode
                        .PAYMENT_ALREADY_EXISTS
        );
    }

    private String generateTransactionKey() {
        return "MOCK-" + UUID.randomUUID();
    }
}
