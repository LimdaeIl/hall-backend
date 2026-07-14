package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.reservation.domain.ReservationSeat;

public record ReservedSeatResponse(
        Long performanceSeatId,
        Long seatId,
        String seatNumber,
        String grade,
        int rowNumber,
        int columnNumber,
        long price
) {

    public static ReservedSeatResponse from(
            ReservationSeat reservationSeat
    ) {
        var performanceSeat =
                reservationSeat.getPerformanceSeat();

        var seat = performanceSeat.getSeat();

        return new ReservedSeatResponse(
                performanceSeat.getId(),
                seat.getId(),
                seat.getSeatNumber(),
                performanceSeat.getGrade().name(),
                seat.getRowNumber(),
                seat.getColumnNumber(),
                reservationSeat.getPrice()
        );
    }
}
