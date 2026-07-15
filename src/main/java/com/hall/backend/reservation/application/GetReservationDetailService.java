package com.hall.backend.reservation.application;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.infrastructure.PaymentRepository;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.exception.ReservationErrorCode;
import com.hall.backend.reservation.exception.ReservationException;
import com.hall.backend.reservation.infrastructure.ReservationRepository;
import com.hall.backend.reservation.presentation.dto.response.GetReservationDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetReservationDetailService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public GetReservationDetailResponse getReservation(Long reservationId,
            MemberPrincipal principal) {
        validateReservationId(reservationId);
        validatePrincipal(principal);

        Reservation reservation = reservationRepository.findDetailByIdAndMemberId(reservationId,
                principal.memberId()).orElseThrow(
                () -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        Payment payment = paymentRepository.findByReservationId(reservationId).orElse(null);

        return GetReservationDetailResponse.of(reservation, payment);
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
