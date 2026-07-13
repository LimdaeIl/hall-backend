package com.hall.backend.payment.presentation.dto.response;


import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.domain.PaymentMethod;
import com.hall.backend.payment.domain.PaymentStatus;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long reservationId,
        long amount,
        PaymentMethod method,
        PaymentStatus status,
        String transactionKey,
        LocalDateTime paidAt
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getReservation().getId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionKey(),
                payment.getPaidAt()
        );
    }
}
