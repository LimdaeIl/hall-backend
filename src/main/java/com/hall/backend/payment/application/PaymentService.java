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

        /*
         * 동일 예약 결제, 취소, 만료 요청을 직렬화합니다.
         */
        Reservation reservation = reservationRepository
                .findByIdForPayment(reservationId)
                .orElseThrow(() -> new ReservationException(
                        ReservationErrorCode.RESERVATION_NOT_FOUND
                ));

        validateOwner(
                reservation,
                memberId
        );

        /*
         * 예약 잠금 획득 후 기존 결제를 확인합니다.
         */
        Payment existingPayment = paymentRepository
                .findByReservationId(reservationId)
                .orElse(null);

        if (existingPayment != null) {
            return handleExistingPayment(existingPayment);
        }

        LocalDateTime now =
                LocalDateTime.now(clock);

        Payment payment = Payment.create(
                reservation,
                reservation.getTotalAmount(),
                method
        );

        payment.approve(
                generateTransactionKey(),
                now
        );

        /*
         * 내부적으로:
         * Reservation PENDING_PAYMENT → COMPLETED
         * PerformanceSeat HELD → RESERVED
         */
        reservation.complete(now);

        return paymentRepository.save(payment);
    }

    private void validateOwner(
            Reservation reservation,
            Long memberId
    ) {
        if (!reservation.isOwnedBy(memberId)) {
            throw new PaymentException(
                    PaymentErrorCode.PAYMENT_ACCESS_DENIED
            );
        }
    }

    private void validatePaymentMethod(
            PaymentMethod method
    ) {
        if (method == null) {
            throw new PaymentException(
                    PaymentErrorCode.PAYMENT_METHOD_REQUIRED
            );
        }

        /*
         * 아직 실제 PG 연동이 없으므로 Mock만 허용합니다.
         */
        if (method != PaymentMethod.MOCK) {
            throw new PaymentException(
                    PaymentErrorCode.UNSUPPORTED_PAYMENT_METHOD
            );
        }
    }

    private Payment handleExistingPayment(
            Payment payment
    ) {
        /*
         * 동일 요청 재시도에 대해 기존 성공 결과를 반환합니다.
         */
        if (payment.getStatus()
                == PaymentStatus.COMPLETED) {
            return payment;
        }

        throw new PaymentException(
                PaymentErrorCode.PAYMENT_ALREADY_EXISTS
        );
    }

    private String generateTransactionKey() {
        return "MOCK-" + UUID.randomUUID();
    }
}
