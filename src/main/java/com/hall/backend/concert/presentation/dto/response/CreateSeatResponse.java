package com.hall.backend.concert.presentation.dto.response;

import com.hall.backend.concert.domain.Seat;

import java.util.List;

public record CreateSeatResponse(
        int createdCount,
        List<SeatResponse> seats
) {

    public static CreateSeatResponse from(List<Seat> seats) {
        List<SeatResponse> responses = seats.stream()
                .map(SeatResponse::from)
                .toList();

        return new CreateSeatResponse(
                responses.size(),
                responses
        );
    }

    public record SeatResponse(
            Long id,
            String seatNumber,
            String grade,
            int rowNumber,
            int columnNumber
    ) {

        public static SeatResponse from(Seat seat) {
            return new SeatResponse(
                    seat.getId(),
                    seat.getSeatNumber(),
                    seat.getGrade().name(),
                    seat.getRowNumber(),
                    seat.getColumnNumber()
            );
        }
    }
}
