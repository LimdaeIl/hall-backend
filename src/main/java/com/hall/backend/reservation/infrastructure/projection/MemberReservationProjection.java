package com.hall.backend.reservation.infrastructure.projection;

import com.hall.backend.payment.domain.PaymentStatus;
import com.hall.backend.performance.domain.PerformanceStatus;
import com.hall.backend.reservation.domain.ReservationStatus;
import java.time.Instant;
import java.time.LocalDateTime;

public interface MemberReservationProjection {

    Long getReservationId();

    ReservationStatus getReservationStatus();

    Long getTotalAmount();

    LocalDateTime getExpiresAt();

    LocalDateTime getCompletedAt();

    LocalDateTime getCancelledAt();

    Instant getCreatedAt();

    Long getPerformanceId();

    LocalDateTime getStartsAt();

    PerformanceStatus getPerformanceStatus();

    Long getConcertId();

    String getConcertTitle();

    String getArtist();

    Long getSeatCount();

    Long getPaymentId();

    PaymentStatus getPaymentStatus();
}
