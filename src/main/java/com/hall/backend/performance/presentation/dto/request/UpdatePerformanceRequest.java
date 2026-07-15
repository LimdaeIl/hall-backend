package com.hall.backend.performance.presentation.dto.request;

import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record UpdatePerformanceRequest(

        LocalDateTime startsAt,

        LocalDateTime reservationOpensAt,

        LocalDateTime reservationClosesAt,

        @Positive(
                message = "회원당 최대 예매 수량은 1 이상이어야 합니다."
        )
        Integer maxTicketsPerMember

) {
}
