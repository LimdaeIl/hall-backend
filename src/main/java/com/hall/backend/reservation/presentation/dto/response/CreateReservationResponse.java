package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationResponse(
        Long reservationId,
        Long performanceId,
        ReservationStatus status,
        List<ReservedSeatResponse> seats,
        long totalAmount,
        LocalDateTime expiresAt
) {

    public static CreateReservationResponse from(
            Reservation reservation
    ) {
        List<ReservedSeatResponse> seats =
                reservation.getReservationSeats()
                        .stream()
                        .map(ReservedSeatResponse::from)
                        .toList();

        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getPerformance().getId(),
                reservation.getStatus(),
                seats,
                reservation.getTotalAmount(),
                reservation.getExpiresAt()
        );
    }
}
