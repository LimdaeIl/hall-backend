package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.domain.PaymentMethod;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.reservation.domain.Reservation;
import com.hall.backend.reservation.domain.ReservationSeat;
import com.hall.backend.reservation.domain.ReservationStatus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record GetReservationDetailResponse(
        Long reservationId,
        Long memberId,
        ReservationStatus status,
        long totalAmount,
        LocalDateTime expiresAt,
        LocalDateTime completedAt,
        LocalDateTime cancelledAt,
        Instant createdAt,
        PerformanceResponse performance,
        List<SeatResponse> seats,
        PaymentResponse payment
) {

    public static GetReservationDetailResponse of(
            Reservation reservation,
            Payment payment
    ) {
        List<SeatResponse> seats =
                reservation.getReservationSeats()
                        .stream()
                        .map(SeatResponse::from)
                        .toList();

        return new GetReservationDetailResponse(
                reservation.getId(),
                reservation.getMember().getId(),
                reservation.getStatus(),
                reservation.getTotalAmount(),
                reservation.getExpiresAt(),
                reservation.getCompletedAt(),
                reservation.getCancelledAt(),
                reservation.getCreatedAt(),
                PerformanceResponse.from(reservation),
                seats,
                payment == null
                        ? null
                        : PaymentResponse.from(payment)
        );
    }

    public record PerformanceResponse(
            Long performanceId,
            Long concertId,
            String concertTitle,
            String artist,
            LocalDateTime startsAt,
            LocalDateTime reservationOpensAt,
            LocalDateTime reservationClosesAt,
            String performanceStatus
    ) {

        public static PerformanceResponse from(
                Reservation reservation
        ) {
            var performance =
                    reservation.getPerformance();

            var concert =
                    performance.getConcert();

            return new PerformanceResponse(
                    performance.getId(),
                    concert.getId(),
                    concert.getTitle(),
                    concert.getArtist(),
                    performance.getStartsAt(),
                    performance.getReservationOpensAt(),
                    performance.getReservationClosesAt(),
                    performance.getStatus().name()
            );
        }
    }

    public record SeatResponse(
            Long reservationSeatId,
            Long performanceSeatId,
            Long seatId,
            String seatNumber,
            String grade,
            int rowNumber,
            int columnNumber,
            long price,
            String status
    ) {

        public static SeatResponse from(
                ReservationSeat reservationSeat
        ) {
            var performanceSeat =
                    reservationSeat.getPerformanceSeat();

            var seat =
                    performanceSeat.getSeat();

            return new SeatResponse(
                    reservationSeat.getId(),
                    performanceSeat.getId(),
                    seat.getId(),
                    seat.getSeatNumber(),
                    performanceSeat.getGrade().name(),
                    seat.getRowNumber(),
                    seat.getColumnNumber(),
                    reservationSeat.getPrice(),
                    performanceSeat.getStatus().name()
            );
        }
    }

    public record PaymentResponse(
            Long paymentId,
            long amount,
            PaymentMethod method,
            PaymentStatus status,
            String transactionKey,
            LocalDateTime paidAt,
            LocalDateTime cancelledAt
    ) {

        public static PaymentResponse from(
                Payment payment
        ) {
            return new PaymentResponse(
                    payment.getId(),
                    payment.getAmount(),
                    payment.getMethod(),
                    payment.getStatus(),
                    payment.getTransactionKey(),
                    payment.getPaidAt(),
                    payment.getCancelledAt()
            );
        }
    }
}
