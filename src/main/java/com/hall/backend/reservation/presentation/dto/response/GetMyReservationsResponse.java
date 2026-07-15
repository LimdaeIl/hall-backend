package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.infrastructure.projection.MemberReservationProjection;
import java.time.Instant;
import java.time.LocalDateTime;

public record GetMyReservationsResponse(
        Long reservationId,
        ReservationStatus reservationStatus,
        long totalAmount,
        long seatCount,
        LocalDateTime expiresAt,
        LocalDateTime completedAt,
        LocalDateTime cancelledAt,
        Instant reservedAt,
        PerformanceResponse performance,
        PaymentResponse payment
) {

    public static GetMyReservationsResponse from(
            MemberReservationProjection projection
    ) {
        PaymentResponse paymentResponse =
                projection.getPaymentId() == null
                        ? null
                        : new PaymentResponse(
                                projection.getPaymentId(),
                                projection.getPaymentStatus()
                        );

        return new GetMyReservationsResponse(
                projection.getReservationId(),
                projection.getReservationStatus(),
                projection.getTotalAmount(),
                projection.getSeatCount() == null
                        ? 0L
                        : projection.getSeatCount(),
                projection.getExpiresAt(),
                projection.getCompletedAt(),
                projection.getCancelledAt(),
                projection.getCreatedAt(),
                new PerformanceResponse(
                        projection.getPerformanceId(),
                        projection.getStartsAt(),
                        projection.getPerformanceStatus(),
                        projection.getConcertId(),
                        projection.getConcertTitle(),
                        projection.getArtist()
                ),
                paymentResponse
        );
    }

    public record PerformanceResponse(
            Long performanceId,
            LocalDateTime startsAt,
            PerformanceStatus status,
            Long concertId,
            String concertTitle,
            String artist
    ) {
    }

    public record PaymentResponse(
            Long paymentId,
            PaymentStatus status
    ) {
    }
}
