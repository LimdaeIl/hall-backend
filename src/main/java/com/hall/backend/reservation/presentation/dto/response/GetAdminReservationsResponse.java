package com.hall.backend.reservation.presentation.dto.response;

import com.hall.backend.payment.domain.PaymentMethod;
import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.reservation.domain.ReservationStatus;
import com.hall.backend.reservation.infrastructure.projection.AdminReservationProjection;
import java.time.Instant;
import java.time.LocalDateTime;

public record GetAdminReservationsResponse(
        Long reservationId,
        ReservationStatus reservationStatus,
        long totalAmount,
        long seatCount,
        LocalDateTime expiresAt,
        LocalDateTime completedAt,
        LocalDateTime cancelledAt,
        Instant reservedAt,
        MemberResponse member,
        PerformanceResponse performance,
        PaymentResponse payment
) {

    public static GetAdminReservationsResponse from(
            AdminReservationProjection projection
    ) {
        PaymentResponse payment =
                projection.getPaymentId() == null
                        ? null
                        : new PaymentResponse(
                                projection.getPaymentId(),
                                projection.getPaymentMethod(),
                                projection.getPaymentStatus(),
                                projection.getTransactionKey(),
                                projection.getPaidAt(),
                                projection.getPaymentCancelledAt()
                        );

        return new GetAdminReservationsResponse(
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
                new MemberResponse(
                        projection.getMemberId(),
                        projection.getMemberEmail()
                ),
                new PerformanceResponse(
                        projection.getPerformanceId(),
                        projection.getStartsAt(),
                        projection.getPerformanceStatus(),
                        projection.getConcertId(),
                        projection.getConcertTitle(),
                        projection.getArtist()
                ),
                payment
        );
    }

    public record MemberResponse(
            Long memberId,
            String email
    ) {
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
            PaymentMethod method,
            PaymentStatus status,
            String transactionKey,
            LocalDateTime paidAt,
            LocalDateTime cancelledAt
    ) {
    }
}
