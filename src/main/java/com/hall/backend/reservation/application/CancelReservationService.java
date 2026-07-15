package com.hall.backend.reservation.application;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.payment.exception.PaymentErrorCode;
import com.hall.backend.payment.exception.PaymentException;
import com.hall.backend.payment.infrastructure.PaymentRepository;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import com.hall.backend.reservation.presentation.dto.response.CancelReservationResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CancelReservationService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final Clock clock;

    @Transactional
    public CancelReservationResponse cancel(Long reservationId, MemberPrincipal principal) {
        validateReservationId(reservationId);
        validatePrincipal(principal);

        Reservation reservation = reservationRepository
                .findByIdAndMemberIdForCancel(reservationId, principal.memberId())
                .orElseThrow(
                        () -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND)
                );

        LocalDateTime now = LocalDateTime.now(clock);
        validateCancellationDeadline(reservation, now);
        validateCancellableStatus(reservation);

        Payment payment = paymentRepository.findByReservationIdForUpdate(reservationId)
                .orElse(null);

        cancelPaymentIfRequired(reservation, payment, now);
        reservation.cancel(now);

        return CancelReservationResponse.of(reservation, payment);
    }

    private void cancelPaymentIfRequired(Reservation reservation, Payment payment,
            LocalDateTime now) {
        if (reservation.getStatus() == ReservationStatus.PENDING_PAYMENT) {
            validatePendingReservationPayment(payment);
            return;
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            if (payment == null) {
                throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND);
            }

            if (payment.getStatus() != PaymentStatus.COMPLETED) {
                throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_COMPLETED);
            }

            payment.cancel(now);
        }
    }

    private void validatePendingReservationPayment(Payment payment) {
        if (payment == null) {
            return;
        }

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new PaymentException(PaymentErrorCode.INVALID_PAYMENT_STATUS);
        }
    }

    private void validateCancellableStatus(Reservation reservation) {
        ReservationStatus status = reservation.getStatus();

        if (status != ReservationStatus.PENDING_PAYMENT && status != ReservationStatus.COMPLETED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_CANCELLABLE);
        }
    }

    private void validateCancellationDeadline(Reservation reservation, LocalDateTime now) {
        LocalDateTime startsAt = reservation.getPerformance().getStartsAt();

        if (!now.isBefore(startsAt)) {
            throw new ReservationException(
                    ReservationErrorCode.RESERVATION_CANCELLATION_DEADLINE_PASSED);
        }
    }

    private void validateReservationId(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    private void validatePrincipal(MemberPrincipal principal) {
        if (principal == null || principal.memberId() == null || principal.memberId() <= 0) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }
}
