package com.hall.backend.payment.presentation;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.payment.application.PaymentService;
import com.hall.backend.payment.domain.Payment;
import com.hall.backend.payment.presentation.dto.request.CreatePaymentRequest;
import com.hall.backend.payment.presentation.dto.response.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{reservationId}/payments")
    public ResponseEntity<PaymentResponse> createPayment(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable Long reservationId,
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        Payment payment = paymentService.pay(principal.memberId(), reservationId, request.method());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PaymentResponse.from(payment));
    }
}
