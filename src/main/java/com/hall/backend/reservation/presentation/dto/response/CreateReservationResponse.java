package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.concert.domain.SeatGrade;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationSeat;
import com.hall.backend.reservation.domain.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CreateReservationResponse(
        Long reservationId,
        String reservationNumber,
        ReservationStatus status,
        long totalAmount,
        LocalDateTime expiredAt,
        List<SeatResponse> seats
) {

    public static CreateReservationResponse from(
            Reservation reservation
    ) {
        List<SeatResponse> seats =
                reservation.getReservationSeats()
                        .stream()
                        .map(SeatResponse::from)
                        .toList();

        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getReservationNumber(),
                reservation.getStatus(),
                reservation.getTotalAmount(),
                reservation.getExpiredAt(),
                seats
        );
    }

    public record SeatResponse(
            String seatNumber,
            SeatGrade grade,
            long price
    ) {

        private static SeatResponse from(
                ReservationSeat reservationSeat
        ) {
            return new SeatResponse(
                    reservationSeat.getSeatNumber(),
                    reservationSeat.getGrade(),
                    reservationSeat.getPrice()
            );
        }
    }
}
