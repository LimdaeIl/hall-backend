package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CancelReservationResponse(
        Long reservationId,
        ReservationStatus reservationStatus,
        LocalDateTime cancelledAt,
        long totalAmount,
        List<CancelledSeatResponse> seats,
        PaymentResponse payment
) {

    public static CancelReservationResponse of(
            Reservation reservation,
            Payment payment
    ) {
        List<CancelledSeatResponse> seats =
                reservation.getReservationSeats()
                        .stream()
                        .map(reservationSeat ->
                                new CancelledSeatResponse(
                                        reservationSeat
                                                .getPerformanceSeat()
                                                .getId(),

                                        reservationSeat
                                                .getPerformanceSeat()
                                                .getSeat()
                                                .getId(),

                                        reservationSeat
                                                .getPerformanceSeat()
                                                .getSeat()
                                                .getSeatNumber(),

                                        reservationSeat
                                                .getPerformanceSeat()
                                                .getStatus()
                                                .name()
                                )
                        )
                        .toList();

        PaymentResponse paymentResponse =
                payment == null
                        ? null
                        : new PaymentResponse(
                                payment.getId(),
                                payment.getStatus(),
                                payment.getCancelledAt()
                        );

        return new CancelReservationResponse(
                reservation.getId(),
                reservation.getStatus(),
                reservation.getCancelledAt(),
                reservation.getTotalAmount(),
                seats,
                paymentResponse
        );
    }

    public record CancelledSeatResponse(
            Long performanceSeatId,
            Long seatId,
            String seatNumber,
            String status
    ) {
    }

    public record PaymentResponse(
            Long paymentId,
            PaymentStatus status,
            LocalDateTime cancelledAt
    ) {
    }
}
