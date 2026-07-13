package com.hall.backend.payment.presentation.dto.request;

import com.hall.backend.payment.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(

        @NotNull(message = "결제 수단은 필수입니다.")
        PaymentMethod method
) {
}
