package com.hall.backend.payment.presentation;

import static com.hall.backend.common.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import com.hall.backend.auth.infrastructure.security.MemberPrincipal;
import com.hall.backend.common.response.ApiResponse;
import com.hall.backend.payment.presentation.dto.request.CreatePaymentRequest;
import com.hall.backend.payment.presentation.dto.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "결제",
        description = "예약 결제 API"
)
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
public interface PaymentControllerDocs {

    @Operation(
            summary = "예약 결제",
            description = """
                    현재 로그인한 회원이 소유한 예약을 결제합니다.
                    요청한 결제 수단을 이용하여 결제를 완료합니다.
                    """
    )
    ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Parameter(hidden = true)
            MemberPrincipal principal,

            @Parameter(
                    description = "결제할 예약 식별자",
                    example = "1",
                    required = true
            )
            Long reservationId,

            CreatePaymentRequest request
    );
}
